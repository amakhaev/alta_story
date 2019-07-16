package com.alta.dao.domain.preservation.global;

import com.alta.dao.data.preservation.GlobalPreservationModel;

/**
 * Provides the service to make CRUD with global preservations.
 */
public interface GlobalPreservationService {

    /**
     * Updates or creates the preservation about quest marked as temporary.
     *
     * @param globalPreservationModel - the model to be saved or updated.
     */
    void upsertTemporaryQuestPreservation(GlobalPreservationModel globalPreservationModel);

    /**
     * Marks the temporary global preservation as completely saved.
     *
     * @param preservationId - the id of preservation.
     */
    void markTemporaryGlobalPreservationAsSaved(Long preservationId);

    /**
     * Gets the temporary global preservation for given preservation id.
     *
     * @param preservationId - the id of preservation.
     * @return found {@link GlobalPreservationModel} instance.
     */
    GlobalPreservationModel getTemporaryGlobalPreservation(Long preservationId);

    /**
     * Gets the global preservation for given preservation id.
     *
     * @param preservationId - the id of preservation.
     * @return found {@link GlobalPreservationModel} instance.
     */
    GlobalPreservationModel getGlobalPreservation(Long preservationId);

}
