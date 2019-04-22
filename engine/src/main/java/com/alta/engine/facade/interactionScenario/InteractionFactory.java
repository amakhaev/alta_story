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

}
