package com.alta.interaction.interactionOnMap;

import com.alta.interaction.scenario.EffectListener;
import com.alta.interaction.scenario.Scenario;
import com.alta.interaction.scenario.ScenarioFactory;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

/**
 * Provides the manager that handles interaction on map.
 */
@Slf4j
public class InteractionOnMapManager {

    private final Scenario scenario;

    private InteractionModel currentInteraction;

    @Setter
    private Function<String, Void> successCallback;

    @Setter
    private Runnable failCallback;

    @Setter
    private List<InteractionModel> interactions;

    /**
     * Initialize new instance of {@link InteractionOnMapManager}.
     *
     * @param effectListener    - the listener of effects.
     * @param scenarioFactory   - the scenario factory.
     */
    @Inject
    public InteractionOnMapManager(EffectListener effectListener, ScenarioFactory scenarioFactory) {
        this.scenario = scenarioFactory.createInteractionScenario(
                effectListener, this::onScenarioCompleted, this::onScenarioFail
        );
    }

    /**
     * Indicates when one of interactions already have ran.
     *
     * @return true if any interaction is ran, false otherwise.
     */
    public boolean isInteractionInProgress() {
        return this.currentInteraction != null;
    }

    /**
     * Starts the interaction for NPC participant.
     *
     * @param targetUuid - the uuid of target participant.
     */
    public void startInteractionForNpc(@NonNull String targetUuid) {
        if (this.isInteractionInProgress()) {
            log.error("One of interactions already have ran: {}", this.currentInteraction.getUuid());
            return;
        }

        this.startInteraction(this.findInteractionForNpc(targetUuid), targetUuid);
    }

    /**
     * Starts the interaction for facility participant.
     *
     * @param targetUuid - the uuid of target participant.
     */
    public void startInteractionForFacility(@NonNull String targetUuid, @NonNull Point shiftTileMapCoordinate) {
        if (this.isInteractionInProgress()) {
            log.error("One of interactions already have ran: {}", this.currentInteraction.getUuid());
            return;
        }

        this.startInteraction(this.findInteractionForFacility(targetUuid, shiftTileMapCoordinate), targetUuid);
    }

    /**
     * Triggers the changing of state for current interaction.
     */
    public void triggerNextStateForInteraction() {
        if (this.isInteractionInProgress()) {
            this.scenario.onNext();
        } else {
            log.warn("No interaction that was ran. Triggering of state unavailable.");
        }
    }

    private synchronized void startInteraction(InteractionModel interactionModel, String targetUuid) {
        if (this.isInteractionInProgress()) {
            log.error("One of interactions already have ran: {}", this.currentInteraction.getUuid());
            return;
        }

        if (interactionModel == null) {
            log.debug("Interaction not found for target uuid {}", targetUuid);
            return;
        }

        this.currentInteraction = interactionModel;
        if (this.currentInteraction.getPreCondition() == null || this.currentInteraction.getPreCondition().apply(null)) {
            this.scenario.performScenario(targetUuid, this.currentInteraction.getInteractionEffects());
        } else {
            this.scenario.performScenario(targetUuid, this.currentInteraction.getFailedPreConditionInteractionEffects());
        }
    }

    private InteractionModel findInteractionForNpc(@NonNull String targetUuid) {
        if (this.interactions == null) {
            return null;
        }

        InteractionModel interaction = this.interactions.stream()
                .filter(interactionModel -> interactionModel.getTargetUuid().equals(targetUuid))
                .findFirst()
                .orElse(null);

        if (interaction == null) {
            return null;
        }

        InteractionModel incompletedInteraction = interaction.findIncompletedInteraction();
        return incompletedInteraction == null ? interaction.findLastInteraction() : incompletedInteraction;
    }

    private InteractionModel findInteractionForFacility(@NonNull String targetUuid,
                                                        @NonNull Point shiftTileMapCoordinate) {
        InteractionModel interaction = this.interactions.stream()
                .filter(interactionModel -> interactionModel.getTargetUuid().equals(targetUuid))
                .findFirst()
                .orElse(null);

        if (interaction == null) {
            return null;
        }

        // Need to run interaction for facility only if select specific targeted tile, not all facility.
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

    private void onScenarioCompleted() {
        if (this.currentInteraction == null) {
            log.warn("Complete interaction was called but no interaction model found.");
            if (this.successCallback != null) {
                this.failCallback.run();
            }
            return;
        }

        this.currentInteraction.setCompleted(true);
        if (this.successCallback != null) {
            this.successCallback.apply(this.currentInteraction.getUuid());
        }

        this.currentInteraction = null;
    }

    private void onScenarioFail() {
        this.currentInteraction = null;
        if (this.failCallback != null) {
            this.failCallback.run();
        }
    }
}
