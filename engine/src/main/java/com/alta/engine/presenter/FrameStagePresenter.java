package com.alta.engine.presenter;

import com.alta.computator.model.event.ActingCharacterJumpEvent;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.engineEventStream.EngineEventStream;
import com.alta.engine.model.JumpingEngineModel;
import com.alta.engine.processing.dataBuilder.FrameStageData;
import com.alta.engine.processing.eventProducer.inputAction.ActionProducer;
import com.alta.engine.processing.listener.engineEvent.EngineListener;
import com.alta.engine.processing.sceneProxy.SceneProxy;
import com.alta.engine.processing.sceneProxy.sceneInput.SceneAction;
import com.alta.engine.view.FrameStageView;
import com.google.inject.Inject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the presenter of view.
 */
@Slf4j
public class FrameStagePresenter {

    private final SceneProxy sceneProxy;
    private final AsyncTaskManager asyncTaskManager;
    private final EngineEventStream<ComputatorEvent> computatorEventStream;

    private FrameStageView currentView;

    @Setter
    private EngineListener engineListener;

    /**
     * Initialize new instance of {@link FrameStagePresenter}
     */
    @Inject
    public FrameStagePresenter(SceneProxy sceneProxy,
                               AsyncTaskManager asyncTaskManager,
                               EngineEventStream<ComputatorEvent> computatorEventStream) {
        this.asyncTaskManager = asyncTaskManager;

        this.sceneProxy = sceneProxy;
        this.sceneProxy.setStateListener(this::onSceneFocusChanged);

        this.computatorEventStream = computatorEventStream;
        this.computatorEventStream.setListener(this::handleComputatorEvent);
        this.computatorEventStream.start();
    }

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStageView(FrameStageData data) {
        this.currentView = new FrameStageView(data, this.asyncTaskManager, this.computatorEventStream);
        this.sceneProxy.renderFrameStage(this.currentView.getFrameStage());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.sceneProxy.sceneStart();
    }

    /**
     * Performs the movement on scene
     *
     * @param movementDirection - the movement that should be performed
     */
    public void movementPerform(MovementDirection movementDirection) {
        if (this.currentView != null) {
            this.currentView.onMovementPerform(movementDirection);
        }
    }

    private void handleComputatorEvent(ComputatorEvent event) {
        if (event == null) {
            log.error("Computator event is null.");
            return;
        }

        if (this.engineListener == null) {
            log.info("No listener for engine. No event will be handled.");
            return;
        }

        if (this.currentView == null) {
            log.info("Current unit is null. No event will be handled.");
            return;
        }

        switch (event.getComputatorEventType()) {
            case ACTING_CHARACTER_JUMP:
                JumpingEngineModel jumpingEngineModel = this.currentView.findJumpingPoint(
                        ((ActingCharacterJumpEvent) event).getMapCoordinates()
                );
                if (jumpingEngineModel != null) {
                    this.engineListener.onJumping(jumpingEngineModel.getMapName(), jumpingEngineModel.getTo());
                }
            default:
                return;
        }
    }

    private void onSceneFocusChanged(boolean sceneHasFocus) {
        if (this.currentView == null) {
            return;
        }
        this.currentView.setPauseComputation(!sceneHasFocus);
    }
}
