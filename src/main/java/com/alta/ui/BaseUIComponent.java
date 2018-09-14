package com.alta.ui;

import lombok.Getter;
import lombok.Setter;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Provides base logic of component
 */
public abstract class BaseUIComponent implements UIComponent {

    @Getter
    @Setter
    private ComponentState state;

    /**
     * Initialize new instance of {@link BaseUIComponent}.
     */
    protected BaseUIComponent() {
        this.setState(ComponentState.PENDING);
    }

    /**
     * Updates the component.
     * Should be called only if state is IN_PROGRESS
     *
     * @param gameContainer - the game componentContainer.
     * @param delta - the last update delta
     */
    @Override
    public final void update(GameContainer gameContainer, int delta) throws SlickException {
        this.onUpdateForInProgressState(gameContainer, delta);
    }

    /**
     * Renders the component.
     * Should be called if state is READY, IN_PROGRESS, DETACH.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    public final void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        switch (this.getState()) {
            case READY:
                this.onRenderForReadyState(gameContainer, graphics);
                break;
            case IN_PROGRESS:
                this.onRenderForInProgressState(gameContainer, graphics);
                break;
            case DETACH:
                this.onRenderForDetachState(gameContainer, graphics);
                break;
        }
    }

    /**
     * Destroy the component.
     */
    @Override
    public void destroy() {
        this.setState(ComponentState.DESTROYED);
    }

    /**
     * Called in update when component state is IN_PROGRESS.
     *
     * @param gameContainer - the game componentContainer.
     * @param delta - the last update delta
     */
    protected void onUpdateForInProgressState(GameContainer gameContainer, int delta) throws SlickException {
    }

    /**
     * Called in render when component state is READY.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    protected void onRenderForReadyState(GameContainer gameContainer, Graphics graphics) throws SlickException {
    }

    /**
     * Called in render when component state is IN_PROGRESS.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    protected void onRenderForInProgressState(GameContainer gameContainer, Graphics graphics) throws SlickException {
    }

    /**
     * Called in render when component state is DETACH.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    protected void onRenderForDetachState(GameContainer gameContainer, Graphics graphics) throws SlickException {
    }
}
