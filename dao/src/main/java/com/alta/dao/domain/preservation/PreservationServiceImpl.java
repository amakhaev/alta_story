package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
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
    private Dao<MapPreservationModel, Long> mapPreservationDao;

    /**
     * Initialize new instance of {@link PreservationServiceImpl}
     */
    @Inject
    public PreservationServiceImpl(ConnectionSource connectionSource) {
        try {
            this.characterPreservationDao = DaoManager.createDao(connectionSource, CharacterPreservationModel.class);
            this.interactionPreservationDao = DaoManager.createDao(connectionSource, InteractionPreservationModel.class);
            this.preservationDao = DaoManager.createDao(connectionSource, PreservationModel.class);
            this.mapPreservationDao = DaoManager.createDao(connectionSource, MapPreservationModel.class);
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

    /**
     * Finds the saved interaction by given preservation id and uuid of interaction.
     *
     * @param preservationId  - the preservation id.
     * @param interactionUuid - the interaction uuid.
     * @return the {@link InteractionPreservationModel} instance or null if not found.
     */
    @Override
    public InteractionPreservationModel findInteractionByPreservationIdAndUuid(Long preservationId,
                                                                               String interactionUuid) {
        try {
            return this.interactionPreservationDao.queryBuilder()
                    .where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(InteractionPreservationModel.UUID_FIELD, interactionUuid)
                    .and()
                    .eq(InteractionPreservationModel.IS_TEMPORARY_FIELD, false)
                    .queryForFirst();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of maps that related to preservation.
     *
     * @param preservationId - the preservation id.
     * @param mapName        - the name of map.
     * @return the {@link List} of {@link MapPreservationModel} related to specific map and preservation.
     */
    @Override
    public List<MapPreservationModel> getMapsPreservation(Long preservationId, String mapName) {
        try {
            return this.mapPreservationDao.queryBuilder()
                    .where()
                    .eq(MapPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(MapPreservationModel.MAP_NAME_FIELD, mapName)
                    .and()
                    .eq(MapPreservationModel.IS_TEMPORARY_FIELD, false)
                    .query();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
