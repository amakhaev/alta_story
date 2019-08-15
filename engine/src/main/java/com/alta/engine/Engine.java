package com.alta.engine;

import com.alta.behaviorprocess.sync.DataType;
import com.alta.behaviorprocess.sync.SynchronizationManager;
import com.alta.engine.configuration.EngineConfiguration;
import com.alta.engine.core.storage.EngineStorage;
import com.alta.engine.data.FrameStageEngineDataModel;
import com.alta.engine.facade.FrameStageFacade;
import com.google.inject.Inject;

import java.util.Arrays;
import java.util.Collections;

/**
 * Provides the engine that contains logic related to scene and calculation
 */
public class Engine {

    private final FrameStageFacade frameStageFacade;
    private final EngineStorage engineStorage;
    private final SynchronizationManager synchronizationManager;

    /**
     * Initialize new instance of {@link Engine}
     */
    @Inject
    public Engine(FrameStageFacade frameStageFacade,
                  EngineConfiguration engineConfiguration,
                  EngineStorage engineStorage,
                  SynchronizationManager synchronizationManager) {
        this.frameStageFacade = frameStageFacade;
        this.engineStorage = engineStorage;
        this.synchronizationManager = synchronizationManager;

        engineConfiguration.configure();
    }

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStage(FrameStageEngineDataModel data) {
        this.engineStorage.put(data);
        this.synchronizationManager.sync(
                Arrays.asList(DataType.CURRENT_MAP, DataType.INTERACTION),
                data.getMapName()
        );
        this.frameStageFacade.tryToRenderFrameStageView();
    }

    /**
     * Runs the initial synchronizations of data.
     */
    public void runInitialSync() {
        this.synchronizationManager.sync(Collections.singletonList(DataType.MAIN_QUEST));
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.frameStageFacade.startScene();
    }
}
