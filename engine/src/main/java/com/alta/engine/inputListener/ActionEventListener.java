package com.alta.engine.inputListener;

/**
 * Provides the action event listener
 */
@FunctionalInterface
public interface ActionEventListener {

    /**
     * Handles the action event
     *
     * @param action - the action that should be performed
     */
    void onActionHandle(SceneAction action);

}
