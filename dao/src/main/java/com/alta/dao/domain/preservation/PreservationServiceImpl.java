package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

/**
 * Provides the service to make CRUD operation with preservation
 */
@Slf4j
public class PreservationServiceImpl implements PreservationService {

    private Dao<CharacterPreservationModel, Integer> characterPreservationDao;
    private Dao<InteractionPreservationModel, Long> interactionPreservationDao;
    private Dao<PreservationModel, Integer> preservationDao;

    /**
     * Initialize new instance of {@link PreservationServiceImpl}
     */
    @Inject
    public PreservationServiceImpl(ConnectionSource connectionSource) {
        try {
            this.characterPreservationDao = DaoManager.createDao(connectionSource, CharacterPreservationModel.class);
            this.interactionPreservationDao = DaoManager.createDao(connectionSource, InteractionPreservationModel.class);
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

    /**
     * Clears all temporary data that related to specific preservation.
     *
     * @param preservationId - the preservation to be cleared.
     */
    @Override
    public void clearTemporaryDataFromPreservation(@NonNull Long preservationId) {
        try {
            DeleteBuilder<InteractionPreservationModel, Long> deleteBuilder = this.interactionPreservationDao.deleteBuilder();
            deleteBuilder.where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(InteractionPreservationModel.IS_TEMPORARY_FIELD, true);
            int deletedCount = deleteBuilder.delete();
            log.info(
                    "Temporary data from preservation with id {} was deleted. Count of removed items: {}",
                    preservationId,
                    deletedCount
            );
        } catch (SQLException e) {
            log.error("Can't clear temporary data for preservation with id {}. Error: {}", preservationId, e.getMessage());
        }
    }

    /**
     * Gets the list of interactions that related to preservation.
     *
     * @param preservationId - the preservation id.
     * @param mapName        - the name of map.
     * @return the {@link List} of {@link InteractionPreservationModel} related to specific map and preservation.
     */
    @Override
    public List<InteractionPreservationModel> getInteractionsPreservation(@NonNull Long preservationId, @NonNull String mapName) {
        try {
            return this.interactionPreservationDao.queryBuilder()
                    .where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(InteractionPreservationModel.MAP_NAME_FIELD, mapName)
                    .and()
                    .eq(InteractionPreservationModel.IS_TEMPORARY_FIELD, false)
                    .query();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
