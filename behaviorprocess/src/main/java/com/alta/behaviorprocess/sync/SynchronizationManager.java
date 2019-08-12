package com.alta.behaviorprocess.sync;

import com.alta.behaviorprocess.data.interaction.InteractionRepository;
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
import java.util.stream.Collectors;

/**
 * Provides the synchronizer of model related to game process.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SynchronizationManager {

    private static final int SYNC_THREAD_COUNT = 3;
    private static final String SYNC_THREAD_POOL_NAME = "model-sync";

    private final InteractionRepository interactionRepository;
    private final QuestRepository questRepository;
    private final DataStorage dataStorage;

    /**
     * Synchronizes the data storage. Data to be synchronized is giving from dataTypes arg.
     *
     * @param dataTypes - the types of data to be synchronized.
     * @param mapName   - the name of map to be used for synchronization.
     */
    public synchronized void sync(List<DataType> dataTypes, String mapName) {
        if (dataTypes == null || dataTypes.isEmpty()) {
            log.warn("Synchronization canceled since no given data types.");
            return;
        }

        log.info(
                "Synchronize job is going to be run with types {}",
                dataTypes.stream().map(DataType::toString).collect(Collectors.joining(","))
        );
        ExecutorService executorService = ExecutorServiceFactory.create(SYNC_THREAD_COUNT, SYNC_THREAD_POOL_NAME);
        try {
            for (DataType dataType: dataTypes) {
                switch (dataType) {
                    case MAIN_QUEST:
                        executorService.submit(this::syncMainQuest);
                        break;
                    case CURRENT_MAP:
                        executorService.submit(() -> this.dataStorage.evictAndSaveCurrentMap(mapName));
                        break;
                    case INTERACTION:
                        executorService.submit(() -> this.syncInteractions(mapName));
                        break;
                    default:
                        log.warn("Unknown type of data synchronization {}", dataType);
                }
            }
        } finally {
            executorService.shutdown();
            log.debug("Shutdown executor service");
        }
    }

    /**
     * Synchronizes the data storage. Data to be synchronized is giving from dataTypes arg.
     *
     * @param dataTypes - the types of data to be synchronized.
     */
    public synchronized void sync(List<DataType> dataTypes) {
        this.sync(dataTypes, null);
    }

    private void syncInteractions(String mapName) {
        if (Strings.isNullOrEmpty(mapName)) {
            log.error("Synchronization of interactions failed since given map name is null or empty.");
            return;
        }

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
