package com.alta.dao.domain.interaction;

import com.alta.dao.data.interaction.InteractionDataModel;

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
    List<InteractionDataModel> getInteractions(String relatedMapName);

    /**
     * Gets the interaction by uuid for given map.
     *
     * @param relatedMapName    - the name of related map.
     * @param interactionUuid   - the uuid of interaction.
     * @return the {@link InteractionDataModel} instance or null if not found.
     */
    InteractionDataModel getInteraction(String relatedMapName, String interactionUuid);
}
