package com.alta.engine.processing.eventProducer.inputAction;

import com.alta.engine.processing.listener.sceneInput.SceneAction;

/**
 * Provides the action event listener
 */
@FunctionalInterface
public interface ActionEventHandler {

    /**
     * Handles the action event
     *
     * @param action - the action that should be performed
     */
    void handle(SceneAction action);

}
