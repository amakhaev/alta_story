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
}
