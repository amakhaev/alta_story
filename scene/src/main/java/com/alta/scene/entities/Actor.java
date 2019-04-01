package com.alta.scene.entities;

import com.alta.scene.core.RenderableEntity;

/**
 * Provides the actor entity for scene
 */
public interface Actor<T> extends RenderableEntity<T> {

    /**
     * Updates the actor state
     *
     * @param delta - the time between last and previous calls
     * @param data  - provides the model to update
     */
    void update(T data, int delta);

}
