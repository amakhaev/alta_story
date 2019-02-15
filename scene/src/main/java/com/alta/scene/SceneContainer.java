package com.alta.scene;

import com.alta.scene.configuration.SceneConfig;
import com.alta.scene.frameStorage.FrameStage;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.*;

import javax.inject.Named;

@Singleton
@Slf4j
public class SceneContainer extends BasicGame {

    @Getter
    private FrameStage currentStage;
    private boolean isNeedToReinit;

    @Setter
    private KeyListener inputListener;

    @Inject
    public SceneContainer(@Named("sceneConfig") SceneConfig config) {
        super(config.getApp().getName());
    }

    /**
     * Sets the stage as current for render
     */
    void setCurrentStage(FrameStage stage) {
        this.currentStage = stage;
        this.isNeedToReinit = true;
    }

    @Override
    public void init(GameContainer gameContainer) {
        if (this.currentStage != null) {
            this.currentStage.onInit(gameContainer);
        }
        gameContainer.getInput().addKeyListener(this.inputListener);
        this.isNeedToReinit = false;
    }

    @Override
    public void update(GameContainer gameContainer, int delta) {
        if (this.isNeedToReinit) {
            try {
                gameContainer.reinit();
            } catch (SlickException e) {
                log.error(e.getMessage());
            }
        }

        if (this.currentStage != null) {
            this.currentStage.onUpdateStage(gameContainer, delta);
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) {
        if (this.currentStage != null) {
            this.currentStage.onRenderFrame(gameContainer, graphics);
        }
    }
}
