package com.alta.scene.core.states;

import com.alta.scene.entities.FrameStage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Provides the state of basic game that tried to render {@link com.alta.scene.entities.FrameStage}
 */
@Slf4j
public class FrameStageState extends BasicGameState {

    private final int stateId;
    private boolean isNeedToReinit;

    @Getter
    private FrameStage currentStage;

    /**
     * Initialize ew instance of {@link FrameStageState}
     */
    public FrameStageState(int stateId) {
        this.stateId = stateId;
    }

    /**
     * Sets the current frame stage to render.
     *
     * @param frameStage - the frame stage to be rendered.
     */
    public void setCurrentStage(FrameStage frameStage) {
        this.currentStage = frameStage;
        this.isNeedToReinit = true;
    }

    @Override
    public int getID() {
        return this.stateId;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) {
        if (this.currentStage != null) {
            this.currentStage.onInit(gameContainer);
        }

        this.isNeedToReinit = false;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        if (this.currentStage != null) {
            this.currentStage.onRenderStage(gameContainer, graphics);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
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
    public void leave(GameContainer container, StateBasedGame game) {
        this.isNeedToReinit = true;
        this.currentStage = null;
    }
}
