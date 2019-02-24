package com.alta.scene;

import com.alta.scene.entities.FrameStage;
import com.alta.scene.entities.FrameStageState;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

/**
 * Provides the base scene object
 */
@Slf4j
public class Scene {

    private AppGameContainer gameContainer;
    private SceneContainer sceneContainer;

    /**
     * Initialize new instance of {@link Scene}
     */
    public Scene() {
        Injector injector = Guice.createInjector(new SceneInjectorModule());
        this.gameContainer = injector.getInstance(AppGameContainer.class);
        this.sceneContainer = injector.getInstance(SceneContainer.class);
    }

    /**
     * Starts the scene
     */
    public void start() {
        try {
            this.gameContainer.start();
        } catch (SlickException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Sets the listener of input on scene
     *
     * @param listener - the listener instance
     */
    public void setInputListener(KeyListener listener) {
        this.sceneContainer.setInputListener(listener);
    }

    /**
     * Sets the frame stage to render
     *
     * @param frameStage - the stage to render
     */
    public void renderStage(FrameStage frameStage) {
        this.sceneContainer.setCurrentStage(frameStage);
    }

}
