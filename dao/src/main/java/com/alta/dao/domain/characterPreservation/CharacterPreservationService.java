package com.alta.dao.domain.characterPreservation;

import com.alta.dao.data.characterPreservation.CharacterPreservationModel;

/**
 * Provides the service to make CRUD operation with characterPreservation
 */
public interface CharacterPreservationService {

    /**
     * Gets the preservation related to character
     *
     * @param id - the identifier preservation of character.
     * @return the {@link CharacterPreservationModel} instance.
     */
    CharacterPreservationModel getCharacterPreservation(Long id);

    /**
     * Updates the preservation that related to character.
     *
     * @param characterPreservationModel - the model to be updated.
     * @return updated {@link CharacterPreservationModel} instance.
     */
    CharacterPreservationModel updateCharacterPreservation(CharacterPreservationModel characterPreservationModel);
}
