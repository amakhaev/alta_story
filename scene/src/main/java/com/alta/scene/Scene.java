package com.alta.scene;

import com.alta.scene.frameStorage.FrameStage;
import com.alta.scene.frameStorage.FrameStageState;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.AppGameContainer;
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
     * Indicates when scene can render next stage
     *
     * @return true if scene can render another stage, false otherwise
     */
    public boolean isReadyForRenderNextStage() {
        FrameStage stage = this.sceneContainer.getCurrentStage();
        return stage != null && stage.getStageState() == FrameStageState.AWAIT_TERMINATION;
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
