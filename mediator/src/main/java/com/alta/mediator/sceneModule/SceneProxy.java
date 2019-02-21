package com.alta.mediator.sceneModule;

import com.alta.mediator.sceneModule.entities.EntityFactory;
import com.alta.scene.Scene;
import com.google.inject.Inject;

/**
 * Provides the proxy object for access to scene
 */
public class SceneProxy {

    private final Scene scene;
    private final EntityFactory entityFactory;

    /**
     * Initialize new instance of {@link SceneProxy}
     */
    @Inject
    public SceneProxy(Scene scene, EntityFactory entityFactory) {
        this.scene = scene;
        this.entityFactory = entityFactory;
    }

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
