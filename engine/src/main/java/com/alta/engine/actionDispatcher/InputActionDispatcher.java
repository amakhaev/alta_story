package com.alta.engine.actionDispatcher;

import com.alta.engine.actionDispatcher.actionHandler.InteractionActionHandler;
import com.alta.engine.actionDispatcher.actionHandler.MoveActionHandler;
import com.alta.engine.actionDispatcher.actionHandler.SaveActionHandler;
import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the dispatcher of actions that comes from keyboard.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InputActionDispatcher {

    private final InteractionActionHandler interactionActionHandler;
    private final MoveActionHandler moveActionHandler;
    private final SaveActionHandler saveActionHandler;

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
        } else if (sceneAction == SceneAction.TEMP_SAVE) {
            this.saveActionHandler.onHandleReleaseAction(sceneAction);
        }
    }
}
