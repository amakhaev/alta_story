package com.alta.interaction.dataSource;

import com.alta.interaction.data.InteractionModel;

/**
 * Provides the repository to make CRUD with interactions.
 */
public interface InteractionRepository {

    /**
     * Finds the interaction for target participant on map.
     *
     * @param mapName       - the name of map where interaction available.
     * @param targetUuid    - the uuid of target for which interaction needed.
     * @return found {@link InteractionModel} instance or null.
     */
    InteractionModel findInteraction(String mapName, String targetUuid);

    /**
     * Completes the interaction.
     *
     * @param interactionUuid   - the uuid of interaction that was completed.
     * @param mapName           - the name of map related to interaction.
     */
    void completeInteraction(String interactionUuid, String mapName);

}
