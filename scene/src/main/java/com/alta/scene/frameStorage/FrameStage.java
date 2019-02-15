package com.alta.scene.frameStorage;

import lombok.Getter;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Provides the stage on scene
 */
public abstract class FrameStage {

    protected final FrameTemplate frameTemplate;

    @Getter
    protected FrameStageState stageState;

    /**
     * Initialize new instance of {@link FrameStage}
     */
    protected FrameStage(FrameTemplate frameTemplate) {
        this.frameTemplate = frameTemplate;
        this.stageState = FrameStageState.CREATED;
    }

    /**
     * Updates the stage
     *
     * @param gameContainer - the game container instance
     * @param delta - the delta between last and current calls
     */
    public abstract void onUpdateStage(GameContainer gameContainer, int delta);

    /**
     * Renders the stage
     *
     * @param gameContainer - the game container instance
     * @param graphics - the graphic to render primitives
     */
    public abstract void onRenderFrame(GameContainer gameContainer, Graphics graphics);

    /**
     * Initializes frame stage in GL context
     *
     * @param gameContainer - the game container instance
     */
    public abstract void onInit(GameContainer gameContainer);
}
