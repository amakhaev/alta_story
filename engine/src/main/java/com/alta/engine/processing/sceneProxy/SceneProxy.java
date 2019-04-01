package com.alta.engine.processing.sceneProxy;

import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.processing.sceneProxy.sceneInput.SceneInputListener;
import com.alta.engine.processing.sceneProxy.sceneState.SceneStateListener;
import com.alta.scene.Scene;
import com.alta.scene.entities.FrameStage;
import com.google.inject.Inject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the sceneProxy of scene.
 */
@Slf4j
public class SceneProxy {

    private static final String SCENE_LISTENING_TASK_NAME = "scene-listener";
    private static final long SCENE_LISTENING_TASK_INTERVAL = 200;

    private final Scene scene;
    private final SceneInputListener inputListener;

    @Setter
    private SceneStateListener stateListener;
    private boolean sceneHasFocus;

    /**
     * Initialize new instance of {@link SceneProxy}
     */
    @Inject
    public SceneProxy(Scene scene, SceneInputListener inputListener, AsyncTaskManager asyncTaskManager) {
        this.scene = scene;
        this.inputListener = inputListener;

        asyncTaskManager.runScheduledTask(
                this::checkSceneState, SCENE_LISTENING_TASK_NAME, SCENE_LISTENING_TASK_INTERVAL
        );
        this.sceneHasFocus = false;
    }

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

    private void checkSceneState() {
        if (this.scene == null || this.stateListener == null) {
            return;
        }

        if (!this.scene.isSceneInitialized()) {
            log.debug("Scene not initialized yet.");
            return;
        }

        if (this.scene.hasFocus() != this.sceneHasFocus) {
            this.sceneHasFocus = this.scene.hasFocus();
            log.info(
                    "The focus state of scene is changed. Previous: {}, current {}",
                    !this.sceneHasFocus,
                    this.sceneHasFocus
            );
            this.stateListener.onFocusStateChanged(this.sceneHasFocus);
        }
    }
}
