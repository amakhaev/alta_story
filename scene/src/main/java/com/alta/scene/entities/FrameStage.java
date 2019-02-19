package com.alta.scene.entities;

import lombok.Getter;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;

/**
 * Provides the stage on scene
 */
public abstract class FrameStage {

    protected final FrameTemplate frameTemplate;
    protected final List<Actor> actors;

    @Getter
    protected FrameStageState stageState;

    /**
     * Initialize new instance of {@link FrameStage}
     */
    protected FrameStage(FrameTemplate frameTemplate, List<Actor> actors) {
        this.frameTemplate = frameTemplate;
        this.actors = actors;
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
    public abstract void onRenderStage(GameContainer gameContainer, Graphics graphics);

    /**
     * Initializes frame stage in GL context
     *
     * @param gameContainer - the game container instance
     */
    public void onInit(GameContainer gameContainer) {
        if (this.frameTemplate != null) {
            this.frameTemplate.initializeFrame();
        }

        if (this.actors != null) {
            this.actors.forEach(Actor::initializeActor);
        }
    }
}
