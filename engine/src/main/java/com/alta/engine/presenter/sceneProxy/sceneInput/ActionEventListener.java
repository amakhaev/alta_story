package com.alta.engine.presenter.sceneProxy.sceneInput;

/**
 * Provides the action event listener
 */
public interface ActionEventListener {

    /**
     * Handles the performing of action.
     *
     * @param action - the action that should be performed.
     */
    void onPerformAction(SceneAction action);

    /**
     * Handles the release of scene action.
     *
     * @param action - the action that should be performed.
     */
    void onPerformActionReleased(SceneAction action);

}
