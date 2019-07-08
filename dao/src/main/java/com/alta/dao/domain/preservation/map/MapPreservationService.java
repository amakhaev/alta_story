package com.alta.dao.domain.preservation.map;

import com.alta.dao.data.preservation.MapPreservationModel;

import java.util.List;

/**
 * Provides the service to make CRUD operation with map preservation.
 */
public interface MapPreservationService {

    /**
     * Gets the list of maps that related to preservation.
     *
     * @param preservationId    - the preservation id.
     * @param mapName           - the name of map.
     * @return the {@link List} of {@link MapPreservationModel} related to specific map and preservation.
     */
    List<MapPreservationModel> getMapsPreservation(Long preservationId, String mapName);

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
    List<MapPreservationModel> getTemporaryMapsPreservation(Long preservationId, String mapName);

    /**
     * Marks the temporary maps as completely saved.
     *
     * @param preservationId - the id of preservation.
     */
    void markTemporaryMapsAsSaved(Long preservationId);

    /**
     * Clears the temporary model related to given preservation.
     *
     * @param preservationId - the id of preservation for which temporary model should be deleted.
     */
    void clearTemporaryData(Long preservationId);

}
