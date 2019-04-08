package com.alta.dao.domain.characterPreservation;

import com.alta.dao.data.characterPreservation.CharacterPreservationModel;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * Provides the service to make CRUD operation with characterPreservation
 */
@Slf4j
public class CharacterPreservationServiceImpl implements CharacterPreservationService {

    private Dao<CharacterPreservationModel, Integer> dao;

    /**
     * Initialize new instance of {@link CharacterPreservationServiceImpl}
     */
    @Inject
    public CharacterPreservationServiceImpl(ConnectionSource connectionSource) {
        try {
            this.dao = DaoManager.createDao(connectionSource, CharacterPreservationModel.class);
        } catch (SQLException e) {
            log.error(e.getMessage());
            this.dao = null;
        }
    }

    /**
     * Gets the preservation related to character
     *
     * @param id - the identifier preservation of character.
     * @return the {@link CharacterPreservationModel} instance.
     */
    @Override
    public CharacterPreservationModel getCharacterPreservation(Long id) {
        try {
            return this.dao.queryBuilder().where().eq(CharacterPreservationModel.ID_FIELD, id).queryForFirst();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Updates the preservation that related to character.
     *
     * @param characterPreservationModel - the model to be updated.
     * @return updated {@link CharacterPreservationModel} instance.
     */
    @Override
    public CharacterPreservationModel updateCharacterPreservation(CharacterPreservationModel characterPreservationModel) {
        if (characterPreservationModel == null || characterPreservationModel.getId() == null) {
            log.error("The model to update is null or has null identifier.");
            throw new IllegalArgumentException("The model to has invalid value.");
        }

        try {
            this.dao.update(characterPreservationModel);
            return this.getCharacterPreservation(characterPreservationModel.getId());
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
