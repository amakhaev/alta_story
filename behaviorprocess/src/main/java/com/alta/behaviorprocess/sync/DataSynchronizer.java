package com.alta.behaviorprocess.sync;

import com.alta.behaviorprocess.behaviorAction.interaction.InteractionRepository;
import com.alta.behaviorprocess.core.DataStorage;
import com.alta.behaviorprocess.shared.data.InteractionModel;
import com.alta.utils.ExecutorServiceFactory;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Provides the synchronizer of data related to game process.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DataSynchronizer {

    private static final int SYNC_THREAD_COUNT = 3;
    private static final String SYNC_THREAD_POOL_NAME = "data-sync";

    private final InteractionRepository interactionRepository;
    private final DataStorage dataStorage;

    /**
     * Synchronizes the data for given map.
     *
     * @param mapName - the name of map for which data should be synchronized.
     */
    public synchronized void synchronize(String mapName) {
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

    private void syncInteractions(String mapName) {
        try {
            log.debug("Start data retrieving of interactions for map {}", mapName);
            List<InteractionModel> interactions = this.interactionRepository.findInteractions(mapName);
            log.debug("Data retrieving of interactions for map {} completed. Found {} interaction(s)", mapName, interactions.size());
            this.dataStorage.evictAndSaveInteractions(interactions);
        } catch (Exception e) {
            log.error("Synchronization of interactions was failed for map " + mapName, e);
        }
    }
}
