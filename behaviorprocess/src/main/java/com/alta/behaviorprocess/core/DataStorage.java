package com.alta.behaviorprocess.core;

import com.alta.behaviorprocess.shared.data.InteractionModel;
import com.alta.caching.Cache;
import com.alta.caching.CacheList;
import com.alta.caching.LazyCache;
import com.alta.caching.LazyCacheList;
import com.google.inject.Inject;

import java.util.List;

/**
 * Provides the storage of data related to all processes.
 */
public class DataStorage {

    private CacheList<InteractionModel> cachedInteractions;
    private Cache<String> cachedCurrentMap;

    /**
     * Initialize new instance of {@link DataStorage}.
     */
    @Inject
    public DataStorage() {
        this.cachedInteractions = new LazyCacheList<>();
        this.cachedCurrentMap = new LazyCache<>();
    }

    /**
     * Replace data in storage related to current map.
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
     * Replace data in storage related to interactions.
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

}
