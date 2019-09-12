package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.*;
import com.alta.dao.domain.preservation.global.GlobalPreservationService;
import com.alta.dao.domain.preservation.interaction.InteractionPreservationService;
import com.alta.dao.domain.preservation.map.MapPreservationService;
import com.alta.dao.domain.preservation.quest.QuestPreservationService;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Provides the service to make CRUD operation with preservation
 */
@Slf4j
public class PreservationServiceImpl implements PreservationService {

    private final ConnectionSource connectionSource;
    private final InteractionPreservationService interactionPreservationService;
    private final MapPreservationService mapPreservationService;
    private final QuestPreservationService questPreservationService;
    private final GlobalPreservationService globalPreservationService;
    private Dao<PreservationModel, Integer> preservationDao;


    /**
     * Initialize new instance of {@link PreservationServiceImpl}
     */
    @Inject
    public PreservationServiceImpl(ConnectionSource connectionSource,
                                   InteractionPreservationService interactionPreservationService,
                                   MapPreservationService mapPreservationService,
                                   QuestPreservationService questPreservationService,
                                   GlobalPreservationService globalPreservationService) {
        this.connectionSource = connectionSource;
        this.interactionPreservationService = interactionPreservationService;
        this.mapPreservationService = mapPreservationService;
        this.questPreservationService = questPreservationService;
        this.globalPreservationService = globalPreservationService;
        try {
            this.preservationDao = DaoManager.createDao(this.connectionSource, PreservationModel.class);
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
            PreservationModel preservationModel = this.preservationDao.queryBuilder()
                    .where()
                    .eq(PreservationModel.ID_FIELD, id)
                    .queryForFirst();

            preservationModel.setGlobalPreservation(this.globalPreservationService.getTemporaryGlobalPreservation(id));
            if (preservationModel.getGlobalPreservation() == null) {
                preservationModel.setGlobalPreservation(this.globalPreservationService.getGlobalPreservation(id));
            }
            return preservationModel;

        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Clears all temporary model that related to specific preservation.
     *
     * @param preservationId - the preservation to be cleared.
     */
    @Override
    public void clearTemporaryDataFromPreservation(@NonNull Long preservationId) {
        try {
            TransactionManager.callInTransaction(this.connectionSource, (Callable<Void>) () -> {
                this.interactionPreservationService.clearTemporaryData(preservationId);
                this.mapPreservationService.clearTemporaryData(preservationId);
                this.questPreservationService.clearTemporaryData(preservationId);
                this.globalPreservationService.clearTemporaryData(preservationId);
                return null;
            });
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Marks all temporary interactions/quests etc. as not temporary.
     *
     * @param preservationId - the preservation id.
     */
    @Override
    public void markTemporaryAsCompletelySaved(Long preservationId) {
        try {
            TransactionManager.callInTransaction(this.connectionSource, (Callable<Void>) () -> {
                this.interactionPreservationService.markTemporaryInteractionsAsSaved(preservationId);
                this.questPreservationService.markTemporaryQuestsAsSaved(preservationId);
                this.mapPreservationService.markTemporaryMapsAsSaved(preservationId);
                this.globalPreservationService.markTemporaryGlobalPreservationAsSaved(preservationId);
                return null;
            });
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
