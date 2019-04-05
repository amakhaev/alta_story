package com.alta.dao.domain.characterPreservation;

import com.alta.dao.data.characterPreservation.CharacterPreservationModel;

/**
 * Provides the service to make CRUD operation with characterPreservation
 */
public interface CharacterPreservationService {

    /**
     * Gets the preservation related to character
     *
     * @param id - the identifier of character.
     * @return the {@link CharacterPreservationModel} instance.
     */
    CharacterPreservationModel getCharacterPreservation(int id);

}
