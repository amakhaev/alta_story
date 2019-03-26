package com.alta.engine;

import com.alta.engine.processing.listener.sceneInput.SceneInputListener;
import com.alta.scene.Scene;
import com.alta.scene.entities.FrameStage;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the proxy object for access to scene
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class SceneProxy {

    private final Scene scene;
    private final SceneInputListener inputListener;

    /**
     * Starts the scene
     */
    void sceneStart() {
        this.scene.setInputListener(this.inputListener);
        this.scene.start();
    }

    /**
     * Renders the frame stage on scene
     *
     * @param frameStage - the frame stage to render
     */
    void renderFrameStage(FrameStage frameStage) {
        this.scene.renderStage(frameStage);
    }

}
