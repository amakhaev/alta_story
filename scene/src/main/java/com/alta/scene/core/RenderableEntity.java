package com.alta.scene.core;

import org.newdawn.slick.GameContainer;

/**
 * Provides the interface of object that can be rendered
 */
public interface RenderableEntity<T> {

    /**
     * Initializes the renderable object in GL context if needed.
     *
     * @param container - the game container instance.
     */
    void initialize(GameContainer container);

    /**
     * Renders the object on given coordinates
     *
     * @param args - the arguments that used for renders
     */
    void render(T args);
}
