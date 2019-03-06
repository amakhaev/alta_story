package com.alta.engine;

import com.alta.engine.entityProvision.FrameStageData;
import com.alta.engine.entityProvision.FrameStageFactory;
import com.alta.engine.entityProvision.entities.BaseFrameStage;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the engine that contains logic related to scene and calculation
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Engine {

    private final SceneProxy sceneProxy;
    private final FrameStageFactory frameStageFactory;

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStage(FrameStageData data) {
        BaseFrameStage frameStage = this.frameStageFactory.createFrameStage(data);
        this.sceneProxy.renderFrameStage(frameStage);
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.sceneProxy.sceneStart();
    }
}
