package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;

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
     * Finds the temporary interaction by given preservation id and uuid of interaction.
     *
     * @param preservationId    - the preservation id.
     * @param interactionUuid   - the interaction uuid.
     * @return the {@link InteractionPreservationModel} instance or null if not found.
     */
    InteractionPreservationModel findInteractionByPreservationIdAndUuid(Long preservationId, String interactionUuid);

    /**
     * Creates or updates the interaction preservation data in storage.
     *
     * @param interactionPreservationModel - the data to be saved.
     */
    void upsertTemporaryInteractionPreservation(InteractionPreservationModel interactionPreservationModel);

    /**
     * Marks all temporary interactions as not temporary on specific map
     *
     * @param preservationId    - the preservation id.
     */
    void markTemporaryInteractionsAsCompletelySaved(Long preservationId);

    /**
     * Creates or updates the preservation of map.
     *
     * @param mapPreservationModel - the preservation that should saved.
     */
    void upsertTemporaryMapPreservation(MapPreservationModel mapPreservationModel);

    /**
     * Finds the temporary preservation related to specific map.
     *
     * @param preservationId    - the id of parent preservation.
     * @param uuid              - the uuid of participant of map.
     * @return the {@link MapPreservationModel} instance or null if not found.
     */
    MapPreservationModel findTemporaryMapPreservation(Long preservationId, String uuid);

    /**
     * Gets the list of maps that related to preservation.
     *
     * @param preservationId    - the preservation id.
     * @param mapName           - the name of map.
     * @return the {@link List} of {@link MapPreservationModel} related to specific map and preservation.
     */
    List<MapPreservationModel> getMapsPreservation(Long preservationId, String mapName);

    /**
     * Clears all temporary data that related to specific preservation.
     *
     * @param preservationId - the preservation to be cleared.
     */
    void clearTemporaryDataFromPreservation(Long preservationId);
}
