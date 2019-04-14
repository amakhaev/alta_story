package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;

/**
 * Provides the service to make CRUD operation with preservation
 */
public interface PreservationService {

    /**
     * Gets the preservation related of game.
     *
     * @param id - the identifier preservation of character.
     * @return the {@link CharacterPreservationModel} instance.
     */
    PreservationModel getPreservation(Long id);

    /**
     * Updates the preservation that related to character.
     *
     * @param characterPreservationModel - the model to be updated.
     */
    void updateCharacterPreservation(CharacterPreservationModel characterPreservationModel);
}
