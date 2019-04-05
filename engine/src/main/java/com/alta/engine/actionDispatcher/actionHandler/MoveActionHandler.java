package com.alta.engine.actionDispatcher.actionHandler;

import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the handler of moving on scene
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MoveActionHandler implements ActionHandler {

    private final FrameStagePresenter frameStagePresenter;

    /**
     * Handles the constantly action from scene.
     *
     * @param action - the action to be handled.
     */
    @Override
    public void onHandleConstantlyAction(SceneAction action) {
        switch (action) {
            case MOVE_UP:
                this.frameStagePresenter.movementPerform(MovementDirection.UP);
                break;
            case MOVE_DOWN:
                this.frameStagePresenter.movementPerform(MovementDirection.DOWN);
                break;
            case MOVE_LEFT:
                this.frameStagePresenter.movementPerform(MovementDirection.LEFT);
                break;
            case MOVE_RIGHT:
                this.frameStagePresenter.movementPerform(MovementDirection.RIGHT);
                break;
        }
    }

    /**
     * Handles the release action from scene.
     *
     * @param action - the action to be handled.
     */
    @Override
    public void onHandleReleaseAction(SceneAction action) {
        log.warn("No need to handle release actions here, method shouldn't be called.");
    }
}
