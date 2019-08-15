package com.alta.dao.domain.preservation.global;

import com.alta.dao.data.preservation.GlobalPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;
import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.domain.preservation.quest.QuestPreservationServiceImpl;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the service to make CRUD with global preservations.
 */
@Slf4j
public class GlobalPreservationServiceImpl implements GlobalPreservationService {

    private Dao<GlobalPreservationModel, Integer> globalPreservationDao;

    /**
     * Initialize new instance of {@link QuestPreservationServiceImpl}
     */
    @Inject
    public GlobalPreservationServiceImpl(ConnectionSource connectionSource) {
        try {
            this.globalPreservationDao = DaoManager.createDao(connectionSource, GlobalPreservationModel.class);
        } catch (SQLException e) {
            log.error("Creating of quest preservation DAO failed", e);
        }
    }

    /**
     * Updates or creates the preservation about quest marked as temporary.
     *
     * @param globalPreservationModel - the model to be saved or updated.
     */
    @Override
    public void upsertTemporaryQuestPreservation(GlobalPreservationModel globalPreservationModel) {
        try {
            globalPreservationModel.setTemporary(true);
            this.globalPreservationDao.createOrUpdate(globalPreservationModel);
        } catch (SQLException e) {
            log.error("Upsert of quest failed", e);
        }
    }

    /**
     * Marks the temporary global preservation as completely saved.
     *
     * @param preservationId - the id of preservation.
     */
    @Override
    public void markTemporaryGlobalPreservationAsSaved(Long preservationId) {
        try {
            List<GlobalPreservationModel> allMaps = this.globalPreservationDao.queryBuilder()
                    .where()
                    .eq(GlobalPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .query();

            List<Long> mapsToBeDeleted = this.findAllSavedPreservationsToBeRemoved(allMaps);
            DeleteBuilder<GlobalPreservationModel, Integer> deleteBuilder = this.globalPreservationDao.deleteBuilder();
            deleteBuilder.where().in(GlobalPreservationModel.ID_FIELD, mapsToBeDeleted);
            deleteBuilder.delete();

            UpdateBuilder<GlobalPreservationModel, Integer> updateBuilder = this.globalPreservationDao.updateBuilder();
            updateBuilder.where().eq(GlobalPreservationModel.PRESERVATION_ID_FIELD, preservationId);
            updateBuilder.updateColumnValue(GlobalPreservationModel.IS_TEMPORARY_FIELD, false);
            updateBuilder.update();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Gets the global preservation for given preservation id.
     *
     * @param preservationId - the id of preservation.
     * @return found {@link GlobalPreservationModel} instance.
     */
    @Override
    public GlobalPreservationModel getTemporaryGlobalPreservation(Long preservationId) {
        try {
            return this.globalPreservationDao.queryBuilder()
                    .where()
                    .eq(GlobalPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(GlobalPreservationModel.IS_TEMPORARY_FIELD, true)
                    .queryForFirst();
        } catch (SQLException e) {
            log.error("Retrieving of temporary global preservation failed", e);
            return null;
        }
    }

    /**
     * Gets the global preservation for given preservation id.
     *
     * @param preservationId - the id of preservation.
     * @return found {@link GlobalPreservationModel} instance.
     */
    @Override
    public GlobalPreservationModel getGlobalPreservation(Long preservationId) {
        try {
            return this.globalPreservationDao.queryBuilder()
                    .where()
                    .eq(GlobalPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(GlobalPreservationModel.IS_TEMPORARY_FIELD, false)
                    .queryForFirst();
        } catch (SQLException e) {
            log.error("Retrieving of temporary global preservation failed", e);
            return null;
        }
    }

    private List<Long> findAllSavedPreservationsToBeRemoved(List<GlobalPreservationModel> globalPreservation) {
        if (globalPreservation == null || globalPreservation.isEmpty()) {
            return Collections.emptyList();
        }

        // The global preservation that were saved but already has new temporary values should be removed.
        List<GlobalPreservationModel> savedGlobalPreservations = globalPreservation.stream()
                .filter(quest -> !quest.isTemporary())
                .collect(Collectors.toList());

        List<GlobalPreservationModel> temporaryGlobalPreservations = globalPreservation.stream()
                .filter(GlobalPreservationModel::isTemporary)
                .collect(Collectors.toList());

        return savedGlobalPreservations.stream()
                .filter(savedQuest -> temporaryGlobalPreservations.stream().anyMatch(
                        temp -> savedQuest.getPreservationId().equals(temp.getPreservationId())
                        )
                )
                .map(GlobalPreservationModel::getId)
                .collect(Collectors.toList());
    }
}
