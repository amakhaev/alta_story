package com.alta.engine.actionDispatcher;

import com.alta.engine.actionDispatcher.actionHandler.InteractionActionHandler;
import com.alta.engine.actionDispatcher.actionHandler.MoveActionHandler;
import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;
import com.google.inject.Inject;

/**
 * Provides the dispatcher of actions that comes from keyboard.
 */
public class InputActionDispatcher {

    private final InteractionActionHandler interactionActionHandler;
    private final MoveActionHandler moveActionHandler;

    /**
     * Initialize new instance of {@link InputActionDispatcher}.
     */
    @Inject
    public InputActionDispatcher(InteractionActionHandler interactionActionHandler,
                                 MoveActionHandler moveActionHandler) {
        this.interactionActionHandler = interactionActionHandler;
        this.moveActionHandler = moveActionHandler;
    }

    /**
     * Dispatches the constant action to be handler.
     *
     * @param sceneAction - the scene action to be perform.
     */
    public void dispatchConstantlyAction(SceneAction sceneAction) {
        switch (sceneAction) {
            case MOVE_UP:
            case MOVE_DOWN:
            case MOVE_LEFT:
            case MOVE_RIGHT:
                this.moveActionHandler.onHandleConstantlyAction(sceneAction);
                break;
        }
    }

    /**
     * Dispatches the release action to be handler.
     *
     * @param sceneAction - the scene action to be perform.
     */
    public void dispatchReleaseAction(SceneAction sceneAction) {
        if (sceneAction == SceneAction.INTERACTION) {
            this.interactionActionHandler.onHandleReleaseAction(sceneAction);
        }
    }
}
