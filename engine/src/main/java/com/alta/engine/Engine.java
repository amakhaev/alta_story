package com.alta.engine;

import com.alta.behaviorprocess.sync.DataSynchronizer;
import com.alta.engine.configuration.EngineConfiguration;
import com.alta.engine.core.storage.EngineStorage;
import com.alta.engine.data.FrameStageEngineDataModel;
import com.alta.engine.facade.FrameStageFacade;
import com.google.inject.Inject;

/**
 * Provides the engine that contains logic related to scene and calculation
 */
public class Engine {

    private final FrameStageFacade frameStageFacade;
    private final EngineStorage engineStorage;
    private final DataSynchronizer dataSynchronizer;

    /**
     * Initialize new instance of {@link Engine}
     */
    @Inject
    public Engine(FrameStageFacade frameStageFacade,
                  EngineConfiguration engineConfiguration,
                  EngineStorage engineStorage,
                  DataSynchronizer dataSynchronizer) {
        this.frameStageFacade = frameStageFacade;
        this.engineStorage = engineStorage;
        this.dataSynchronizer = dataSynchronizer;

        engineConfiguration.configure();
    }

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStage(FrameStageEngineDataModel data) {
        this.engineStorage.put(data);
        this.dataSynchronizer.synchronizeAllDependsOnMap(data.getMapName());
        this.frameStageFacade.tryToRenderFrameStageView();
    }

    /**
     * Runs the initial synchronizations of data.
     */
    public void runInitialSync() {
        this.dataSynchronizer.synchronizeQuests();
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.frameStageFacade.startScene();
    }
}
