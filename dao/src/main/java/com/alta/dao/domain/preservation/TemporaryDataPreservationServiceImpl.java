package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Provides the service to make CRUD with temporary data in preservation.
 */
@Slf4j
public class TemporaryDataPreservationServiceImpl implements TemporaryDataPreservationService {

    private final ConnectionSource connectionSource;
    private Dao<InteractionPreservationModel, Long> interactionPreservationDao;
    private Dao<MapPreservationModel, Long> mapPreservationDao;

    /**
     * Initialize new instance of {@link PreservationServiceImpl}
     */
    @Inject
    public TemporaryDataPreservationServiceImpl(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            this.interactionPreservationDao = DaoManager.createDao(connectionSource, InteractionPreservationModel.class);
            this.mapPreservationDao = DaoManager.createDao(connectionSource, MapPreservationModel.class);
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
    public List<InteractionPreservationModel> getTemporaryInteractionsPreservation(Long preservationId, String mapName) {
        try {
            return this.interactionPreservationDao.queryBuilder()
                    .where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(InteractionPreservationModel.MAP_NAME_FIELD, mapName)
                    .and()
                    .eq(InteractionPreservationModel.IS_TEMPORARY_FIELD, true)
                    .query();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the interaction preservation.
     *
     * @param preservationId  - the preservation id.
     * @param interactionUuid - the interaction uuid.
     * @return the {@link InteractionPreservationModel} instance.
     */
    @Override
    public InteractionPreservationModel getTemporaryInteractionPreservation(Long preservationId, String interactionUuid) {
        try {
            return this.interactionPreservationDao.queryBuilder()
                    .where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(InteractionPreservationModel.UUID_FIELD, interactionUuid)
                    .and()
                    .eq(InteractionPreservationModel.IS_TEMPORARY_FIELD, true)
                    .queryForFirst();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Finds the temporary interaction by given preservation id and uuid of interaction.
     *
     * @param preservationId  - the preservation id.
     * @param interactionUuid - the interaction uuid.
     * @return the {@link InteractionPreservationModel} instance or null if not found.
     */
    @Override
    public InteractionPreservationModel findInteractionByPreservationIdAndUuid(Long preservationId, String interactionUuid) {
        try {
            return this.interactionPreservationDao.queryBuilder()
                    .where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(InteractionPreservationModel.UUID_FIELD, interactionUuid)
                    .and()
                    .eq(InteractionPreservationModel.IS_TEMPORARY_FIELD, true)
                    .queryForFirst();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Creates or updates the interaction preservation model in storage.
     *
     * @param interactionPreservationModel - the model to be saved.
     */
    @Override
    public void upsertTemporaryInteractionPreservation(@NonNull InteractionPreservationModel interactionPreservationModel) {
        try {
            interactionPreservationModel.setTemporary(true);
            this.interactionPreservationDao.createOrUpdate(interactionPreservationModel);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Marks all temporary interactions as not temporary on specific map
     *
     * @param preservationId - the preservation id.
     */
    @Override
    public void markTemporaryInteractionsAsCompletelySaved(Long preservationId) {
        try {
            TransactionManager.callInTransaction(this.connectionSource, (Callable<Void>) () -> {
                this.markTemporaryInteractionsAsSaved(preservationId);
                this.markTemporaryMapsAsSaved(preservationId);
                return null;
            });
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Creates or updates the preservation of map.
     *
     * @param mapPreservationModel - the preservation that should saved.
     */
    @Override
    public void upsertTemporaryMapPreservation(@NonNull MapPreservationModel mapPreservationModel) {
        try {
            mapPreservationModel.setTemporary(true);
            this.mapPreservationDao.createOrUpdate(mapPreservationModel);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Finds the temporary preservation related to specific map.
     *
     * @param preservationId - the id of parent preservation.
     * @param uuid           - the uuid of participant of map.
     * @return the {@link MapPreservationModel} instance or null if not found.
     */
    @Override
    public MapPreservationModel findTemporaryMapPreservation(@NonNull Long preservationId, @NonNull String uuid) {
        try {
            return this.mapPreservationDao.queryBuilder()
                    .where()
                    .eq(MapPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(MapPreservationModel.UUID_FIELD, uuid)
                    .and()
                    .eq(MapPreservationModel.IS_TEMPORARY_FIELD, true)
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
                    .eq(MapPreservationModel.IS_TEMPORARY_FIELD, true)
                    .query();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Clears all temporary data that related to specific preservation.
     *
     * @param preservationId - the preservation to be cleared.
     */
    @Override
    public void clearTemporaryDataFromPreservation(@NonNull Long preservationId) {
        this.clearTemporaryDataFromInteractionPreservation(preservationId);
        this.clearTemporaryDataFromMapPreservation(preservationId);
    }

    private void clearTemporaryDataFromInteractionPreservation(@NonNull Long preservationId) {
        try {
            DeleteBuilder<InteractionPreservationModel, Long> deleteBuilder = this.interactionPreservationDao.deleteBuilder();
            deleteBuilder.where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(InteractionPreservationModel.IS_TEMPORARY_FIELD, true);
            int deletedCount = deleteBuilder.delete();
            log.info(
                    "Temporary data from interaction preservation with id {} was deleted. Count of removed items: {}",
                    preservationId,
                    deletedCount
            );
        } catch (SQLException e) {
            log.error("Can't clear temporary data for interaction preservation with id {}. Error: {}", preservationId, e.getMessage());
        }
    }

    private void clearTemporaryDataFromMapPreservation(@NonNull Long preservationId) {
        try {
            DeleteBuilder<MapPreservationModel, Long> deleteBuilder = this.mapPreservationDao.deleteBuilder();
            deleteBuilder.where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(InteractionPreservationModel.IS_TEMPORARY_FIELD, true);
            int deletedCount = deleteBuilder.delete();
            log.info(
                    "Temporary data from map preservation with id {} was deleted. Count of removed items: {}",
                    preservationId,
                    deletedCount
            );
        } catch (SQLException e) {
            log.error("Can't clear temporary data for map preservation with id {}. Error: {}", preservationId, e.getMessage());
        }
    }

    private void markTemporaryInteractionsAsSaved(Long preservationId) {
        try {
            List<InteractionPreservationModel> allInteractions = this.interactionPreservationDao.queryBuilder()
                    .where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .query();

            List<Long> interactionToBeDeleted = this.findAllSavedInteractionsToBeRemoved(allInteractions);
            DeleteBuilder<InteractionPreservationModel, Long> deleteBuilder = this.interactionPreservationDao.deleteBuilder();
            deleteBuilder.where().in(InteractionPreservationModel.ID_FIELD, interactionToBeDeleted);
            deleteBuilder.delete();

            UpdateBuilder<InteractionPreservationModel, Long> updateBuilder = this.interactionPreservationDao.updateBuilder();
            updateBuilder.where().eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId);
            updateBuilder.updateColumnValue(InteractionPreservationModel.IS_TEMPORARY_FIELD, false);
            updateBuilder.update();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private void markTemporaryMapsAsSaved(Long preservationId) {
        try {
            List<MapPreservationModel> allMaps = this.mapPreservationDao.queryBuilder()
                    .where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .query();

            List<Long> mapsToBeDeleted = this.findAllSavedMapsToBeRemoved(allMaps);
            DeleteBuilder<MapPreservationModel, Long> deleteBuilder = this.mapPreservationDao.deleteBuilder();
            deleteBuilder.where().in(MapPreservationModel.ID_FIELD, mapsToBeDeleted);
            deleteBuilder.delete();

            UpdateBuilder<MapPreservationModel, Long> updateBuilder = this.mapPreservationDao.updateBuilder();
            updateBuilder.where().eq(MapPreservationModel.PRESERVATION_ID_FIELD, preservationId);
            updateBuilder.updateColumnValue(MapPreservationModel.IS_TEMPORARY_FIELD, false);
            updateBuilder.update();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private List<Long> findAllSavedInteractionsToBeRemoved(List<InteractionPreservationModel> interactions) {
        if (interactions == null || interactions.size() == 0) {
            return Collections.emptyList();
        }

        // The interactions that were saved but already has new temporary values should be removed.
        List<InteractionPreservationModel> savedInteractions = interactions.stream()
                .filter(interaction -> !interaction.isTemporary())
                .collect(Collectors.toList());

        List<InteractionPreservationModel> temporaryInteractions = interactions.stream()
                .filter(InteractionPreservationModel::isTemporary)
                .collect(Collectors.toList());

        return savedInteractions.stream()
                .filter(savedInteraction -> temporaryInteractions.stream().anyMatch(
                        temp -> savedInteraction.getPreservationId().equals(temp.getPreservationId()) &&
                                savedInteraction.getUuid().equals(temp.getUuid()) &&
                                savedInteraction.getMapName().equals(temp.getMapName())
                        )
                )
                .map(InteractionPreservationModel::getId)
                .collect(Collectors.toList());
    }

    private List<Long> findAllSavedMapsToBeRemoved(List<MapPreservationModel> maps) {
        if (maps == null || maps.size() == 0) {
            return Collections.emptyList();
        }

        // The maps that were saved but already has new temporary values should be removed.
        List<MapPreservationModel> savedInteractions = maps.stream()
                .filter(interaction -> !interaction.isTemporary())
                .collect(Collectors.toList());

        List<MapPreservationModel> temporaryMaps = maps.stream()
                .filter(MapPreservationModel::isTemporary)
                .collect(Collectors.toList());

        return savedInteractions.stream()
                .filter(savedMap -> temporaryMaps.stream().anyMatch(
                        temp -> savedMap.getPreservationId().equals(temp.getPreservationId()) &&
                                savedMap.getParticipantUuid().equals(temp.getParticipantUuid()) &&
                                savedMap.getMapName().equals(temp.getMapName())
                        )
                )
                .map(MapPreservationModel::getId)
                .collect(Collectors.toList());
    }
}
