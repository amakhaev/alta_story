package com.alta.engine.actionDispatcher;

import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;

/**
 * Provides the interface to handle the action.
 */
public interface ActionHandler {

    /**
     * Handles the constantly action from scene.
     *
     * @param action - the action to be handled.
     */
    void onHandleConstantlyAction(SceneAction action);

    /**
     * Handles the release action from scene.
     *
     * @param action - the action to be handled.
     */
    void onHandleReleaseAction(SceneAction action);

}
