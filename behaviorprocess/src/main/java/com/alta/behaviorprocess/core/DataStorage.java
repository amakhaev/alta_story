package com.alta.behaviorprocess.core;

import com.alta.behaviorprocess.data.interaction.InteractionModel;
import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.caching.Cache;
import com.alta.caching.CacheList;
import com.alta.caching.LazyCache;
import com.alta.caching.LazyCacheList;
import com.google.inject.Inject;

import java.util.List;

/**
 * Provides the storage of model related to all processes.
 */
public class DataStorage {

    private CacheList<InteractionModel> cachedInteractions;
    private Cache<String> cachedCurrentMap;
    private Cache<QuestModel> cachedMainQuest;

    /**
     * Initialize new instance of {@link DataStorage}.
     */
    @Inject
    public DataStorage() {
        this.cachedInteractions = new LazyCacheList<>();
        this.cachedCurrentMap = new LazyCache<>();
        this.cachedMainQuest = new LazyCache<>();
    }

    /**
     * Replace model in storage related to current map.
     *
     * @param mapName - the name of map to be saved.
     */
    public synchronized void evictAndSaveCurrentMap(String mapName) {
        this.cachedCurrentMap.push(mapName);
    }

    /**
     * Gets the name of current map.
     */
    public String getCurrentMap() {
        return this.cachedCurrentMap.get();
    }

    /**
     * Replace model in storage related to interactions.
     *
     * @param interactions - the interactions to be saved in storage.
     */
    public synchronized void evictAndSaveInteractions(List<InteractionModel> interactions) {
        this.cachedInteractions.evictAndPushAll(interactions);
    }

    /**
     * Gets the list of cached interactions.
     */
    public List<InteractionModel> getInteractions() {
        return this.cachedInteractions.get();
    }

    /**
     * Replace model in storage related to main quest.
     *
     * @param questModel - the main quest to be saved in storage.
     */
    public synchronized void evictAndSaveMainQuest(QuestModel questModel) {
        this.cachedMainQuest.push(questModel);
    }

    /**
     * Gets the list of cached main quest.
     */
    public QuestModel getMainQuest() {
        return this.cachedMainQuest.get();
    }

}
