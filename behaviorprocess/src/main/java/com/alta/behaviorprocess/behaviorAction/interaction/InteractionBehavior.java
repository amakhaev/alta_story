package com.alta.behaviorprocess.behaviorAction.interaction;

import com.alta.behaviorprocess.behaviorAction.Behavior;
import com.alta.behaviorprocess.core.DataStorage;
import com.alta.behaviorprocess.shared.data.EffectModel;
import com.alta.behaviorprocess.shared.data.InteractionModel;
import com.alta.behaviorprocess.shared.scenario.InteractionScenario;
import com.alta.behaviorprocess.shared.scenario.Scenario;
import com.alta.behaviorprocess.shared.scenario.ScenarioFactory;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectListener;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;

/**
 * Provides the behavior processor for interaction.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InteractionBehavior implements Behavior<InteractionScenarioData> {

    private final InteractionRepository interactionRepository;
    private final ScenarioFactory scenarioFactory;
    private final EffectListener effectListener;
    private final DataStorage dataStorage;

    /**
     * Gets the scenario by given params.
     *
     * @param scenarioParams - the params that applied to scenario.
     * @return created {@link Scenario} instance.
     */
    @Override
    public synchronized Scenario getScenario(@NonNull InteractionScenarioData scenarioParams) {
        if (Strings.isNullOrEmpty(this.dataStorage.getCurrentMap()) || Strings.isNullOrEmpty(scenarioParams.getTargetUuid())) {
            throw new RuntimeException("Can't create the scenario since map name and/or targetUuid is null");
        }

        InteractionModel interactionModel = scenarioParams.getShiftTileMapCoordinate() == null ?
                this.findInteractionForNpc(scenarioParams.getTargetUuid()) :
                this.findInteractionForFacility(scenarioParams.getTargetUuid(), scenarioParams.getShiftTileMapCoordinate());

        if (interactionModel == null) {
            return null;
        }

        List<EffectModel> effects = interactionModel.getPreCondition() == null ||
                interactionModel.getPreCondition().apply(null) ?
                interactionModel.getInteractionEffects() :
                interactionModel.getFailedPreConditionInteractionEffects();

        InteractionScenario scenario = this.scenarioFactory.createInteractionScenario(
                this.effectListener,
                scenarioParams.getTargetUuid(),
                effects
        );
        scenario.subscribeToResult(
                () -> this.onScenarioCompleted(interactionModel),
                () -> this.onScenarioFailed(interactionModel.getUuid())
        );

        return scenario;
    }

    private InteractionModel findInteractionForNpc(@NonNull String targetUuid) {
        InteractionModel interaction = this.findInteractionByTargetUuid(targetUuid);

        if (interaction == null) {
            return null;
        }

        InteractionModel incompletedInteraction = interaction.findIncompletedInteraction();
        return incompletedInteraction == null ? interaction.findLastInteraction() : incompletedInteraction;
    }

    private InteractionModel findInteractionForFacility(@NonNull String targetUuid, @NonNull Point shiftTileMapCoordinate) {
        InteractionModel interaction = this.findInteractionByTargetUuid(targetUuid);

        if (interaction == null) {
            return null;
        }

        // Need to run interaction for facility only if select specific targeted part(tile), not all facility.
        if (!interaction.getShiftTiles().isEmpty()) {
            Point interactionTile = interaction.getShiftTiles().stream()
                    .filter(shiftTile -> shiftTile.x == shiftTileMapCoordinate.x && shiftTile.y == shiftTileMapCoordinate.y)
                    .findFirst()
                    .orElse(null);

            if (interactionTile == null) {
                log.debug("The tile for interaction not found for given related coordinates {}", shiftTileMapCoordinate);
                return null;
            }
        }

        InteractionModel incompletedInteraction = interaction.findIncompletedInteraction();
        return incompletedInteraction == null ? interaction.findLastInteraction() : incompletedInteraction;
    }

    private InteractionModel findInteractionByTargetUuid(String targetUuid) {
        List<InteractionModel> interactionModels = this.dataStorage.getInteractions();
        if (interactionModels == null) {
            return null;
        }

        return interactionModels.stream()
                .filter(i -> i.getTargetUuid().equals(targetUuid))
                .findFirst()
                .orElse(null);
    }

    private void onScenarioCompleted(InteractionModel interactionModel) {
        this.interactionRepository.completeInteraction(interactionModel.getUuid(), this.dataStorage.getCurrentMap());
        interactionModel.setCompleted(true);
        log.debug("Interaction {} on map {} was completed.", interactionModel.getUuid(), this.dataStorage.getCurrentMap());
    }

    private void onScenarioFailed(String interactionUuid) {
        log.debug("Interaction {} on map {} was failed.", interactionUuid, this.dataStorage.getCurrentMap());
    }
}
