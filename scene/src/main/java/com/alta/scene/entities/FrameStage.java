package com.alta.scene.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;

/**
 * Provides the stage on scene
 */
public abstract class FrameStage {

    protected final FrameTemplate frameTemplate;
    protected final List<Actor> actors;
    protected final List<? extends Facility> facilities;

    /**
     * Initialize new instance of {@link FrameStage}
     */
    protected FrameStage(FrameTemplate frameTemplate, List<Actor> actors, List<? extends Facility> facilities) {
        this.frameTemplate = frameTemplate;
        this.actors = actors;
        this.facilities = facilities;
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
            this.frameTemplate.initialize(gameContainer);
        }

        if (this.facilities != null) {
            this.facilities.forEach(facility -> facility.initialize(gameContainer));
        }

        if (this.actors != null) {
            this.actors.forEach(Actor::initializeActor);
        }
    }
}
