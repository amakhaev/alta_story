package com.alta.engine;

import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.inputListener.ActionProducer;
import com.alta.engine.processing.dataBuilder.FrameStageData;
import com.alta.engine.processing.EngineUnit;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the engine that contains logic related to scene and calculation
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Engine {

    private final SceneProxy sceneProxy;
    private final AsyncTaskManager asyncTaskManager;
    private final ActionProducer actionProducer;

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStage(FrameStageData data) {
        EngineUnit engineUnit = new EngineUnit(data, this.actionProducer, this.asyncTaskManager);
        this.sceneProxy.renderFrameStage(engineUnit.getFrameStage());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.sceneProxy.sceneStart();
    }
}
