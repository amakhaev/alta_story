package com.alta.engine.facade;

import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.engine.facade.interactionScenario.InteractionScenario;
import com.alta.engine.facade.interactionScenario.InteractionScenarioFactory;
import com.alta.engine.model.InteractionDataModel;
import com.alta.engine.model.interaction.InteractionEngineModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the facade to cooperation interaction in engine.
 */
@Slf4j
public class InteractionFacade {

    private final FrameStagePresenter frameStagePresenter;
    private final InteractionScenario interactionScenario;
    private final FrameStageListener frameStageListener;

    private InteractionEngineModel currentInteraction;

    @Setter
    private InteractionDataModel interactionData;

    /**
     * Initialize ew instance of {@link InteractionFacade}.
     */
    @Inject
    public InteractionFacade(FrameStagePresenter frameStagePresenter,
                             InteractionScenarioFactory interactionScenarioFactory,
                             FrameStageListener frameStageListener) {
        this.frameStagePresenter = frameStagePresenter;
        this.interactionScenario = interactionScenarioFactory.createInteractionScenario(
                this::onScenarioCompletedSuccesfully, this::onScenarioFail
        );
        this.frameStageListener = frameStageListener;
    }

    /**
     * Triggers the interaction between acting character and another participant.
     */
    public void triggerInteraction() {
        if (this.interactionData == null) {
            log.error("The interaction data not fount.");
        }

        if (this.currentInteraction != null) {
            this.interactionScenario.onNext();
        } else {
            this.startInteraction();
        }
    }

    private void startInteraction() {
        TargetedParticipantSummary targetedParticipant = this.frameStagePresenter.findParticipantTargetedByActingCharacter();
        if (targetedParticipant == null) {
            log.debug("Participant for interaction not found.");
            return;
        }

        InteractionEngineModel interaction = null;
        if (targetedParticipant.getParticipatType() == ParticipatType.SIMPLE_NPC ||
                targetedParticipant.getParticipatType() == ParticipatType.ROUTE_NPC) {
            interaction = this.findInteractionForNpc(targetedParticipant.getUuid());
        } else if (targetedParticipant.getParticipatType() == ParticipatType.FACILITY) {
            interaction = this.findInteractionForFacility(targetedParticipant.getUuid(), targetedParticipant.getRelatedMapCoordinates());
        }


        if (interaction == null) {
            log.debug("Interaction not found for target uuid {}", targetedParticipant.getUuid());
            return;
        }

        this.currentInteraction = interaction;
        if (interaction.getPreCondition() == null || interaction.getPreCondition().apply(null)) {
            this.interactionScenario.performScenario(targetedParticipant, interaction.getInteractionEffects());
        } else {
            this.interactionScenario.performScenario(targetedParticipant, interaction.getFailedPreConditionInteractionEffects());
        }
    }

    private InteractionEngineModel findInteractionForNpc(@NonNull String targetUuid) {
        InteractionEngineModel interaction = this.interactionData.getInteractions()
                .stream()
                .filter(interactionModel -> interactionModel.getTargetUuid().equals(targetUuid))
                .findFirst()
                .orElse(null);

        if (interaction == null) {
            return null;
        }

        InteractionEngineModel incompletedInteraction = interaction.findIncompletedInteraction();
        return incompletedInteraction == null ? interaction.findLastInteraction() : incompletedInteraction;
    }

    private InteractionEngineModel findInteractionForFacility(@NonNull String targetUuid,
                                                              @NonNull Point shiftTileMapCoordinate) {
        InteractionEngineModel interaction = this.interactionData.getInteractions()
                .stream()
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

        InteractionEngineModel incompletedInteraction = interaction.findIncompletedInteraction();
        return incompletedInteraction == null ? interaction.findLastInteraction() : incompletedInteraction;
    }

    private void onScenarioCompletedSuccesfully() {
        if (this.currentInteraction == null) {
            log.warn("Complete interaction was called but no interaction model found.");
            return;
        }

        this.currentInteraction.setCompleted(true);
        this.frameStageListener.handleInteractionCompleteEvent(this.currentInteraction.getUuid());
        this.currentInteraction = null;
    }

    private void onScenarioFail() {
        this.currentInteraction = null;
    }
}
