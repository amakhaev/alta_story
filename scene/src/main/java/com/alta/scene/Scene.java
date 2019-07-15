package com.alta.scene;

import com.alta.scene.entities.FrameStage;
import com.google.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

/**
 * Provides the base scene object
 */
@Slf4j
public class Scene {

    private final AppGameContainer gameContainer;
    private final SceneContainer sceneContainer;

    @Getter
    private boolean isSceneInitialized;

    /**
     * Initialize new instance of {@link Scene}
     */
    @Inject
    public Scene(AppGameContainer gameContainer, SceneContainer sceneContainer) {
        this.gameContainer = gameContainer;
        this.sceneContainer = sceneContainer;
        this.isSceneInitialized = false;
    }

    /**
     * Starts the scene.
     */
    public void start() {
        try {
            this.isSceneInitialized = true;
            this.gameContainer.start();
        } catch (SlickException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Sets the listener of input on scene.
     *
     * @param listener - the listener instance.
     */
    public void setInputListener(KeyListener listener) {
        this.sceneContainer.setInputListener(listener);
    }

    /**
     * Sets the frame stage to render
     *
     * @param frameStage - the stage to render.
     */
    public void renderStage(FrameStage frameStage) {
        this.sceneContainer.renderStage(frameStage);
    }

    /**
     * Indicates when scene (widow) has a focus.
     *
     * @return true if scene has a focus, false otherwise.
     */
    public boolean hasFocus() {
        return this.gameContainer.hasFocus();
    }
}
