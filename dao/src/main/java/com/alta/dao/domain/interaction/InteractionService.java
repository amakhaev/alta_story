package com.alta.dao.domain.interaction;

import com.alta.dao.data.interaction.InteractionModel;

import java.util.List;

/**
 * Provides the service to make CRUD operations with interactions.
 */
public interface InteractionService {

    /**
     * Gets the list of interactions that available on specific map.
     *
     * @param relatedMapName - the name of related map.
     * @return the {@link List} of interactions.
     */
    List<InteractionModel> getInteractions(String relatedMapName);

    /**
     * Gets the interaction by uuid for given map.
     *
     * @param relatedMapName    - the name of related map.
     * @param interactionUuid   - the uuid of interaction.
     * @return the {@link InteractionModel} instance or null if not found.
     */
    InteractionModel getInteraction(String relatedMapName, String interactionUuid);
}
