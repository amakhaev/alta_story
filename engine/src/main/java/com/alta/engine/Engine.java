package com.alta.engine;

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

    /**
     * Initialize new instance of {@link Engine}
     */
    @Inject
    public Engine(FrameStageFacade frameStageFacade,
                  EngineConfiguration engineConfiguration,
                  EngineStorage engineStorage) {
        this.frameStageFacade = frameStageFacade;
        this.engineStorage = engineStorage;
        engineConfiguration.configure();
    }

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStage(FrameStageEngineDataModel data) {
        this.engineStorage.put(data);
        this.frameStageFacade.tryToRenderFrameStageView();
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.frameStageFacade.startScene();
    }
}
