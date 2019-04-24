package com.alta.engine.facade.interactionScenario;

import com.alta.computator.model.participant.TargetedParticipantSummary;

/**
 * Provides the factory to create interactions.
 */
public interface InteractionFactory {

    /**
     * Creates the instance of {@link DialogueInteraction}.
     */
    DialogueInteraction createDialogueInteraction(TargetedParticipantSummary targetedParticipantSummary);

    /**
     * Creates tje instance of {@link HideFacilityInteraction}.
     *
     * @return created {@link HideFacilityInteraction} instance.
     */
    HideFacilityInteraction createHideFacilityInteraction();

    /**
     * Creates tje instance of {@link ShowFacilityInteraction}.
     *
     * @return created {@link ShowFacilityInteraction} instance.
     */
    ShowFacilityInteraction createShowFacilityInteraction();

}
