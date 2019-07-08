package com.alta.behaviorprocess.data.interaction;

import java.util.List;

/**
 * Provides the repository to make CRUD with interactions.
 */
public interface InteractionRepository {

    /**
     * Finds the interactions for target map.
     *
     * @param mapName       - the name of map where interaction available.
     * @return found {@link List< InteractionModel >} instance.
     */
    List<InteractionModel> findInteractions(String mapName);

    /**
     * Completes the interaction.
     *
     * @param interactionUuid   - the uuid of interaction that was completed.
     * @param mapName           - the name of map related to interaction.
     */
    void completeInteraction(String interactionUuid, String mapName);

}
