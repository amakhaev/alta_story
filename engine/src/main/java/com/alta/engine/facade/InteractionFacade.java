package com.alta.engine.facade;

import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.interaction.interactionOnMap.InteractionOnMapManager;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the facade to cooperation interaction in engine.
 */
@Slf4j
public class InteractionFacade {

    private final FrameStagePresenter frameStagePresenter;
    private final FrameStageListener frameStageListener;
    private final InteractionOnMapManager interactionOnMapManager;

    /**
     * Initialize ew instance of {@link InteractionFacade}.
     */
    @Inject
    public InteractionFacade(FrameStagePresenter frameStagePresenter,
                             FrameStageListener frameStageListener,
                             InteractionOnMapManager interactionOnMapManager) {
        this.frameStagePresenter = frameStagePresenter;
        this.frameStageListener = frameStageListener;
        this.interactionOnMapManager = interactionOnMapManager;
        this.interactionOnMapManager.setSuccessCallback(this::onScenarioCompletedSuccessfully);
    }

    /**
     * Triggers the interaction between acting character and another participant.
     */
    public void triggerInteraction() {
        if (this.interactionOnMapManager.isInteractionInProgress()) {
            this.interactionOnMapManager.triggerNextStateForInteraction();
            return;
        }

        TargetedParticipantSummary targetedParticipant = this.frameStagePresenter.findParticipantTargetedByActingCharacter();
        if (targetedParticipant == null) {
            log.debug("Participant for interaction not found.");
            return;
        }

        switch (targetedParticipant.getParticipatType()) {
            case SIMPLE_NPC:
            case ROUTE_NPC:
                this.interactionOnMapManager.startInteractionForNpc(targetedParticipant.getUuid());
                break;
            case FACILITY:
                this.interactionOnMapManager.startInteractionForFacility(
                        targetedParticipant.getUuid(), targetedParticipant.getRelatedMapCoordinates()
                );
                break;
        }
    }

    private Void onScenarioCompletedSuccessfully(String interactionUuid) {
        this.frameStageListener.handleInteractionCompleteEvent(interactionUuid);
        return null;
    }
}
