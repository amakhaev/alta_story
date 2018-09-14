package com.alta.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Provides the base UI component.
 */
public interface UIComponent {

    /**
     * Init the UI component.
     *
     * @param gameContainer - the game componentContainer.
     */
    void init(GameContainer gameContainer) throws SlickException;

    /**
     * Updates the component.
     *
     * @param gameContainer - the game componentContainer.
     * @param delta - the last update delta
     */
    void update(GameContainer gameContainer, int delta) throws SlickException;

    /**
     * Renders the component.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    void render(GameContainer gameContainer, Graphics graphics) throws SlickException;

    /**
     * Destroy the component.
     */
    void destroy();
}
