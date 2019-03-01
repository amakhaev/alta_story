package com.alta.mediator.sceneModule;

import com.alta.scene.Scene;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the proxy object for access to scene
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SceneProxy {

    private final Scene scene;
    private final EntityFactory entityFactory;

    /**
     * Starts the scene
     */
    public void sceneStart() {
        this.scene.start();
    }

    /**
     * Loads scene state from preservation
     */
    public void loadSceneFromPreservation() {
        this.scene.renderStage(this.entityFactory.createFrameStageFromPreservation());
    }

}
