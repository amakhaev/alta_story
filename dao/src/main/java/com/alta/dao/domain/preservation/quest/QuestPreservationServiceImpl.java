package com.alta.dao.domain.preservation.quest;

import com.alta.dao.data.preservation.QuestPreservationModel;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class QuestPreservationServiceImpl implements QuestPreservationService {

    private Dao<QuestPreservationModel, Integer> questPreservationDao;

    /**
     * Initialize new instance of {@link QuestPreservationServiceImpl}
     */
    @Inject
    public QuestPreservationServiceImpl(ConnectionSource connectionSource) {
        try {
            this.questPreservationDao = DaoManager.createDao(connectionSource, QuestPreservationModel.class);
        } catch (SQLException e) {
            log.error("Creating of quest preservation DAO failed", e);
        }
    }

    /**
     * Gets the quest by given by given preservation id and uuid of quest uuid.
     *
     * @param preservationId - the id of preservation.
     * @param name           - the name of quest to be retrieved.
     * @return found {@link QuestPreservationModel} instance.
     */
    @Override
    public QuestPreservationModel getQuestPreservation(Long preservationId, String name) {
        try {
            return this.questPreservationDao.queryBuilder()
                    .where()
                    .eq(QuestPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(QuestPreservationModel.NAME_FIELD, name)
                    .and()
                    .eq(QuestPreservationModel.IS_TEMPORARY_FIELD, false)
                    .queryForFirst();
        } catch (SQLException e) {
            log.error("The quest retrieving was failed", e);
            return null;
        }
    }

    /**
     * Gets the temporary quest by given by given preservation id and uuid of quest uuid.
     *
     * @param preservationId - the id of preservation.
     * @param name           - the name of quest to be retrieved.
     * @return found {@link QuestPreservationModel} instance.
     */
    @Override
    public QuestPreservationModel getTemporaryQuestPreservation(Long preservationId, String name) {
        try {
            return this.questPreservationDao.queryBuilder()
                    .where()
                    .eq(QuestPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(QuestPreservationModel.NAME_FIELD, name)
                    .and()
                    .eq(QuestPreservationModel.IS_TEMPORARY_FIELD, true)
                    .queryForFirst();
        } catch (SQLException e) {
            log.error("The temporary quest retrieving was failed", e);
            return null;
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
            DeleteBuilder<QuestPreservationModel, Integer> deleteBuilder = this.questPreservationDao.deleteBuilder();
            deleteBuilder.where()
                    .eq(QuestPreservationModel.PRESERVATION_ID_FIELD, preservationId)
                    .and()
                    .eq(QuestPreservationModel.IS_TEMPORARY_FIELD, true);
            int deletedCount = deleteBuilder.delete();
            log.info(
                    "Temporary model from quest preservation with id {} was deleted. Count of removed items: {}",
                    preservationId,
                    deletedCount
            );
        } catch (SQLException e) {
            log.error("Can't clear temporary model for quest preservation with id {}. Error: {}", preservationId, e.getMessage());
        }
    }
}
