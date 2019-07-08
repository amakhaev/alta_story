package com.alta.dao.domain.preservation.map;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the service to make CRUD operation with map preservation.
 */
@Slf4j
public class MapPreservationServiceImpl implements MapPreservationService {

    private Dao<MapPreservationModel, Long> mapPreservationDao;

    /**
     * Initialize new instance of {@link MapPreservationServiceImpl}
     */
    @Inject
    public MapPreservationServiceImpl(ConnectionSource connectionSource) {
        try {
            this.mapPreservationDao = DaoManager.createDao(connectionSource, MapPreservationModel.class);
        } catch (SQLException e) {
            log.error("Creating of map preservation DAO failed", e);
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
    public List<MapPreservationModel> getTemporaryMapsPreservation(Long preservationId, String mapName) {
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
     * Marks the temporary maps as completely saved.
     *
     * @param preservationId - the id of preservation.
     */
    @Override
    public void markTemporaryMapsAsSaved(Long preservationId) {
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

    /**
     * Clears the temporary model related to given preservation.
     *
     * @param preservationId - the id of preservation for which temporary model should be deleted.
     */
    @Override
    public void clearTemporaryData(Long preservationId) {
        try {
            DeleteBuilder<MapPreservationModel, Long> deleteBuilder = this.mapPreservationDao.deleteBuilder();
            deleteBuilder.where()
                    .eq(InteractionPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(InteractionPreservationModel.IS_TEMPORARY_FIELD, true);
            int deletedCount = deleteBuilder.delete();
            log.info(
                    "Temporary model from map preservation with id {} was deleted. Count of removed items: {}",
                    preservationId,
                    deletedCount
            );
        } catch (SQLException e) {
            log.error("Can't clear temporary model for map preservation with id {}. Error: {}", preservationId, e.getMessage());
        }
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
