package com.alta.dao.domain.preservation.interaction;

import com.alta.dao.data.preservation.InteractionPreservationModel;

import java.util.List;

/**
 * Provides the service to make CRUD operation with interaction preservation.
 */
public interface InteractionPreservationService {

    /**
     * Gets the list of interactions that related to preservation.
     *
     * @param preservationId    - the preservation id.
     * @param mapName           - the name of map.
     * @return the {@link List} of {@link InteractionPreservationModel} related to specific map and preservation.
     */
    List<InteractionPreservationModel> getInteractionsPreservation(Long preservationId, String mapName);

    /**
     * Finds the saved interaction by given preservation id and uuid of interaction.
     *
     * @param preservationId    - the preservation id.
     * @param interactionUuid   - the interaction uuid.
     * @return the {@link InteractionPreservationModel} instance or null if not found.
     */
    InteractionPreservationModel findInteractionByPreservationIdAndUuid(Long preservationId, String interactionUuid);

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
     * Finds the temporary interaction by given preservation id and uuid of interaction.
     *
     * @param preservationId    - the preservation id.
     * @param interactionUuid   - the interaction uuid.
     * @return the {@link InteractionPreservationModel} instance or null if not found.
     */
    InteractionPreservationModel findTemporaryInteractionByPreservationIdAndUuid(Long preservationId, String interactionUuid);

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

    /**
     * Clears the temporary model related to given preservation.
     *
     * @param preservationId - the id of preservation for which temporary model should be deleted.
     */
    void clearTemporaryData(Long preservationId);

}
