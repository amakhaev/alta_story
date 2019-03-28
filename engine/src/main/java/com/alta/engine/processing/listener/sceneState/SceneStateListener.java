package com.alta.engine.processing.listener.sceneState;

/**
 * Provides the listener of scene.
 */
public interface SceneStateListener {

    /**
     * Handles the changing of focus state
     *
     * @param hasFocus - indicates if scene has a focus.
     */
    void onFocusStateChanged(boolean hasFocus);

}
