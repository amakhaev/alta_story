package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.InteractionPreservationModel;

import java.util.List;

/**
 * Provides the service to make CRUD with temporary data in preservation.
 */
public interface TemporaryDataPreservationService {

    /**
     * Gets the list of interactions that related to preservation.
     *
     * @param preservationId    - the preservation id.
     * @param mapName           - the name of map.
     * @return the {@link List} of {@link InteractionPreservationModel} related to specific map and preservation.
     */
    List<InteractionPreservationModel> getTemporaryInteractionsPreservation(Long preservationId, String mapName);

    /**
     * Gets the interaction preservation.
     *
     * @param preservationId    - the preservation id.
     * @param interactionUuid   - the interaction uuid.
     * @return the {@link InteractionPreservationModel} instance.
     */
    InteractionPreservationModel getTemporaryInteractionPreservation(Long preservationId, String interactionUuid);

    /**
     * Creates or updates the interaction preservation model in storage.
     *
     * @param interactionPreservationModel - the model to be saved.
     */
    void upsertTemporaryInteractionPreservation(InteractionPreservationModel interactionPreservationModel);

    /**
     * Marks all temporary interactions as not temporary on specific map
     *
     * @param preservationId    - the preservation id.
     */
    void markTemporaryInteractionsAsCompletelySaved(Long preservationId);
}
