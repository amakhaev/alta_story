package com.alta.behaviorprocess.sync;

import com.alta.behaviorprocess.data.interaction.InteractionRepository;
import com.alta.behaviorprocess.core.DataStorage;
import com.alta.behaviorprocess.data.interaction.InteractionModel;
import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.behaviorprocess.data.quest.QuestRepository;
import com.alta.utils.ExecutorServiceFactory;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Provides the synchronizer of model related to game process.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DataSynchronizer {

    private static final int SYNC_THREAD_COUNT = 3;
    private static final String SYNC_THREAD_POOL_NAME = "model-sync";

    private final InteractionRepository interactionRepository;
    private final QuestRepository questRepository;
    private final DataStorage dataStorage;

    /**
     * Synchronizes the model for given map.
     *
     * @param mapName - the name of map for which model should be synchronized.
     */
    public synchronized void synchronizeAllDependsOnMap(String mapName) {
        if (Strings.isNullOrEmpty(mapName)) {
            throw new RuntimeException("The name of map is not specified.");
        }

        log.info("Synchronize job is running for map {}", mapName);
        ExecutorService executorService = ExecutorServiceFactory.create(SYNC_THREAD_COUNT, SYNC_THREAD_POOL_NAME);
        try {
            executorService.submit(() -> this.syncInteractions(mapName));
            executorService.submit(() -> this.dataStorage.evictAndSaveCurrentMap(mapName));
        } finally {
            executorService.shutdown();
            log.debug("Shutdown executor service");
        }
    }

    /**
     * Synchronizes the quests.
     */
    public synchronized void synchronizeQuests() {
        log.info("Synchronize job for quests is running");
        ExecutorService executorService = ExecutorServiceFactory.create(SYNC_THREAD_COUNT, SYNC_THREAD_POOL_NAME);
        try {
            executorService.submit(this::syncMainQuest);
        } finally {
            executorService.shutdown();
            log.debug("Synchronize job for quests completed.");
        }
    }

    private void syncInteractions(String mapName) {
        try {
            log.debug("Start model retrieving of interactions for map {}", mapName);
            List<InteractionModel> interactions = this.interactionRepository.findInteractions(mapName);
            log.debug("Data retrieving of interactions for map {} completed. Found {} interaction(s)", mapName, interactions.size());
            this.dataStorage.evictAndSaveInteractions(interactions);
        } catch (Exception e) {
            log.error("Synchronization of interactions was failed for map " + mapName, e);
        }
    }

    private void syncMainQuest() {
        try {
            log.debug("Start retrieving the main quest");
            QuestModel questModel = this.questRepository.getMainQuest();
            log.debug("Main quest retrieving completed.");
            this.dataStorage.evictAndSaveMainQuest(questModel);
        } catch (Exception e) {
            log.error("Synchronization of main quest was failed", e);
        }
    }
}
