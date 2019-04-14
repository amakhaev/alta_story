package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * Provides the service to make CRUD operation with preservation
 */
@Slf4j
public class PreservationServiceImpl implements PreservationService {

    private Dao<CharacterPreservationModel, Integer> characterPreservationDao;
    private Dao<PreservationModel, Integer> preservationDao;

    /**
     * Initialize new instance of {@link PreservationServiceImpl}
     */
    @Inject
    public PreservationServiceImpl(ConnectionSource connectionSource) {
        try {
            this.characterPreservationDao = DaoManager.createDao(connectionSource, CharacterPreservationModel.class);
            this.preservationDao = DaoManager.createDao(connectionSource, PreservationModel.class);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Gets the preservation related of game.
     *
     * @param id - the identifier preservation of character.
     * @return the {@link CharacterPreservationModel} instance.
     */
    @Override
    public PreservationModel getPreservation(Long id) {
        try {
            return this.preservationDao.queryBuilder().where().eq(PreservationModel.ID_FIELD, id).queryForFirst();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
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
            log.error(e.getMessage());
        }
    }
}
