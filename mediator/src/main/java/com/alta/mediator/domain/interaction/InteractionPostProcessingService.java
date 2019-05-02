package com.alta.mediator.domain.interaction;

/**
 * Provides the service that handles the post processors related to interaction.
 */
public interface InteractionPostProcessingService {

    /**
     * Executes the post processors for interaction.
     *
     * @param uuid      - the uuid of interaction.
     * @param mapName   - the name of map with interaction.
     */
    void executeInteractionPostProcessing(String uuid, String mapName);

}
