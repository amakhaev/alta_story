package com.alta.engine.facade.interactionScenario;

/**
 * Provides the factory to create interactions.
 */
public interface InteractionFactory {

    /**
     * Creates the instance of {@link DialogueInteraction}.
     */
    DialogueInteraction createDialogueInteraction(String targetUuid);

}
