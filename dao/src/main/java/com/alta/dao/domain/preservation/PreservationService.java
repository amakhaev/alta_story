package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.PreservationModel;

/**
 * Provides the service to make CRUD operation with preservation
 */
public interface PreservationService {

    /**
     * Gets the preservation related of game.
     *
     * @param id - the identifier preservation of character.
     * @return the {@link PreservationModel} instance.
     */
    PreservationModel getPreservation(Long id);

    /**
     * Clears all temporary model that related to specific preservation.
     *
     * @param preservationId - the preservation to be cleared.
     */
    void clearTemporaryDataFromPreservation(Long preservationId);

    /**
     * Marks all temporary interactions/quests etc. as not temporary.
     *
     * @param preservationId    - the preservation id.
     */
    void markTemporaryAsCompletelySaved(Long preservationId);
}
