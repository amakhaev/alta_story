package com.alta.scene;

import com.alta.scene.configuration.SceneConfig;
import com.alta.scene.frameStorage.FrameStage;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import javax.inject.Named;

@Singleton
@Slf4j
public class SceneContainer extends BasicGame {

    @Getter
    private FrameStage currentStage;
    private boolean isNeedToReinit;

    @Inject
    public SceneContainer(@Named("sceneConfig") SceneConfig config) {
        super(config.getApp().getName());
    }

    /**
     * Sets the stage as current for render
     */
    public void setCurrentStage(FrameStage stage) {
        this.currentStage = stage;
        this.isNeedToReinit = true;
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        if (this.currentStage != null) {
            this.currentStage.onInit(gameContainer);
        }
        this.isNeedToReinit = false;
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        if (this.currentStage != null) {
            this.currentStage.onUpdateStage(gameContainer, delta);
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        if (this.currentStage != null) {
            this.currentStage.onRenderFrame(gameContainer, graphics);
        }
    }
}
