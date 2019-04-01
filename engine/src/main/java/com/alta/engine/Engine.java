package com.alta.engine;

import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import com.alta.engine.processing.dataBuilder.FrameStageData;
import com.alta.engine.processing.eventProducer.inputAction.ActionProducer;
import com.alta.engine.processing.listener.engineEvent.EngineListener;
import com.alta.engine.processing.sceneProxy.sceneInput.SceneAction;
import com.google.inject.Inject;

/**
 * Provides the engine that contains logic related to scene and calculation
 */
public class Engine {

    private final FrameStagePresenter frameStagePresenter;
    private final MessageBoxPresenter messageBoxPresenter;

    /**
     * Initialize new instance of {@link Engine}
     */
    @Inject
    public Engine(FrameStagePresenter frameStagePresenter,
                  MessageBoxPresenter messageBoxPresenter,
                  ActionProducer actionProducer) {
        this.frameStagePresenter = frameStagePresenter;
        this.messageBoxPresenter = messageBoxPresenter;

        actionProducer.setListener(this::dispatchInputActionEvent);
    }

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStage(FrameStageData data) {
        this.frameStagePresenter.tryToRenderFrameStageView(data);
        this.messageBoxPresenter.showTitle(data.getMapDisplayName());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.frameStagePresenter.startScene();
    }

    /**
     * Sets the listener of events from engine.
     *
     * @param engineListener - the listener of engine events.
     */
    public void setEngineListener(EngineListener engineListener) {
        this.frameStagePresenter.setEngineListener(engineListener);
    }

    private void dispatchInputActionEvent(SceneAction action) {
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
            case INTERACTION:
                this.messageBoxPresenter.showMessage("My simple message");
                break;
        }
    }
}
