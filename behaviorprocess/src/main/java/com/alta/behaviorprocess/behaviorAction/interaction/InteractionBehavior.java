package com.alta.behaviorprocess.behaviorAction.interaction;

import com.alta.behaviorprocess.behaviorAction.Behavior;
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

    /**
     * Gets the scenario by given params.
     *
     * @param scenarioParams - the params that applied to scenario.
     * @return created {@link Scenario} instance.
     */
    @Override
    public synchronized Scenario getScenario(@NonNull InteractionScenarioData scenarioParams) {
        if (Strings.isNullOrEmpty(scenarioParams.getMapName()) || Strings.isNullOrEmpty(scenarioParams.getTargetUuid())) {
            throw new RuntimeException("Can't create the scenario since map name and/or targetUuid is null");
        }

        InteractionModel interactionModel = scenarioParams.getShiftTileMapCoordinate() == null ?
                this.findInteractionForNpc(scenarioParams.getMapName(), scenarioParams.getTargetUuid()) :
                this.findInteractionForFacility(
                        scenarioParams.getMapName(), scenarioParams.getTargetUuid(), scenarioParams.getShiftTileMapCoordinate()
                );

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
                () -> this.onScenarioCompleted(interactionModel.getUuid(), scenarioParams.getMapName()),
                () -> this.onScenarioFailed(interactionModel.getUuid(), scenarioParams.getMapName())
        );

        return scenario;
    }

    private InteractionModel findInteractionForNpc(@NonNull String mapName, @NonNull String targetUuid) {
        InteractionModel interaction = this.interactionRepository.findInteraction(mapName, targetUuid);

        if (interaction == null) {
            return null;
        }

        InteractionModel incompletedInteraction = interaction.findIncompletedInteraction();
        return incompletedInteraction == null ? interaction.findLastInteraction() : incompletedInteraction;
    }

    private InteractionModel findInteractionForFacility(@NonNull String mapName,
                                                        @NonNull String targetUuid,
                                                        @NonNull Point shiftTileMapCoordinate) {
        InteractionModel interaction = this.interactionRepository.findInteraction(mapName, targetUuid);

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

    private void onScenarioCompleted(String interactionUuid, String mapName) {
        this.interactionRepository.completeInteraction(interactionUuid, mapName);
        log.debug("Interaction {} on map {} was completed.", interactionUuid, mapName);
    }

    private void onScenarioFailed(String interactionUuid, String mapName) {
        log.debug("Interaction {} on map {} was failed.", interactionUuid, mapName);
    }
}
