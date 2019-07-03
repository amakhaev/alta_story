package com.alta.engine.core.storage;

import com.alta.caching.Cache;
import com.alta.caching.LazyCache;
import com.alta.engine.data.FrameStageEngineDataModel;
import com.google.inject.Inject;

/**
 * Provides the storage for data into engine.
 */
public class EngineStorage {

    private final Cache<FrameStageEngineDataModel> frameStageDataCache;

    /**
     * Initialize new instance of {@link EngineStorage}.
     */
    @Inject
    public EngineStorage() {
        this.frameStageDataCache = new LazyCache<>();
    }

    /**
     * Puts the frame stage data to storage.
     *
     * @param data - the data to be saved.
     */
    public void put(FrameStageEngineDataModel data) {
        this.frameStageDataCache.push(data);
    }

    /**
     * Gets the frame stage data.
     *
     * @return the {@link FrameStageEngineDataModel} instance or null if ot found.
     */
    public FrameStageEngineDataModel getFrameStageData() {
        return this.frameStageDataCache.get();
    }
}
