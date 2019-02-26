package com.alta.scene.core;

import org.newdawn.slick.GameContainer;

/**
 * Provides the interface of object that can be rendered
 */
public interface RenderableEntity {

    /**
     * Initializes the renderable object in GL context if needed.
     *
     * @param container - the game container instance.
     */
    void initialize(GameContainer container);

    /**
     * Renders the object on given coordinates
     *
     * @param x - start coordinate on X axis
     * @param y - start coordinate on Y axis
     */
    void render(int x, int y);

}
