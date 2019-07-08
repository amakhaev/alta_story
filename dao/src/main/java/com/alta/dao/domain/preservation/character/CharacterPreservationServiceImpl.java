package com.alta.dao.domain.preservation.character;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * Provides the preservation service to make CRUD for character.
 */
@Slf4j
public class CharacterPreservationServiceImpl implements CharacterPreservationService {

    private Dao<CharacterPreservationModel, Integer> characterPreservationDao;

    /**
     * Initialize new instance of {@link CharacterPreservationServiceImpl}
     */
    @Inject
    public CharacterPreservationServiceImpl(ConnectionSource connectionSource) {
        try {
            this.characterPreservationDao = DaoManager.createDao(connectionSource, CharacterPreservationModel.class);
        } catch (SQLException e) {
            log.error("Creating of character preservation DAO failed", e);
        }
    }

    /**
     * Updates the preservation that related to character.
     *
     * @param characterPreservationModel - the model to be updated.
     */
    @Override
    public void updateCharacterPreservation(CharacterPreservationModel characterPreservationModel) {
        if (characterPreservationModel == null || characterPreservationModel.getId() == null) {
            log.error("The model to update is null or has null identifier.");
            throw new IllegalArgumentException("The model to has invalid value.");
        }

        try {
            this.characterPreservationDao.update(characterPreservationModel);
        } catch (SQLException e) {
            log.error("Updating of character preservation failed", e);
        }
    }

}
