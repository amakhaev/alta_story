package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.InteractionPreservationModel;
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

    /**
     * Initialize new instance of {@link PreservationServiceImpl}
     */
    @Inject
    public TemporaryDataPreservationServiceImpl(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            this.interactionPreservationDao = DaoManager.createDao(connectionSource, InteractionPreservationModel.class);
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

                return null;
            });
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
}
