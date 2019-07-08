package com.alta.dao.domain.preservation.character;

import com.alta.dao.data.preservation.CharacterPreservationModel;

/**
 * Provides the preservation service to make CRUD for character.
 */
public interface CharacterPreservationService {

    /**
     * Updates the preservation that related to character.
     *
     * @param characterPreservationModel - the model to be updated.
     */
    void updateCharacterPreservation(CharacterPreservationModel characterPreservationModel);

}
