package com.alta.engine;

import com.alta.engine.entityProvision.FrameStageFactory;
import com.alta.engine.inputListener.SceneInputListener;
import com.alta.scene.Scene;
import com.alta.scene.entities.FrameStage;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the proxy object for access to scene
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SceneProxy {

    private final Scene scene;
    private final FrameStageFactory frameStageFactory;
    private final SceneInputListener inputListener;

    /**
     * Starts the scene
     */
    public void sceneStart() {
        this.scene.setInputListener(this.inputListener);
        this.scene.start();
    }

    /**
     * Renders the frame stage on scene
     *
     * @param frameStage - the frame stage to render
     */
    public void renderFrameStage(FrameStage frameStage) {
        this.scene.renderStage(frameStage);
    }

}
