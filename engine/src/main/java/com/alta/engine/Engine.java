package com.alta.engine;

import com.alta.computator.model.event.ActingCharacterJumpEvent;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.engieEventStream.EngineEventStream;
import com.alta.engine.data.JumpingEngineModel;
import com.alta.engine.processing.EngineUnit;
import com.alta.engine.processing.dataBuilder.FrameStageData;
import com.alta.engine.processing.eventProducer.inputAction.ActionProducer;
import com.alta.engine.processing.listener.engineEvent.EngineListener;
import com.google.inject.Inject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the engine that contains logic related to scene and calculation
 */
@Slf4j
public class Engine {

    private final SceneProxy sceneProxy;
    private final AsyncTaskManager asyncTaskManager;
    private final ActionProducer actionProducer;
    private final EngineEventStream<ComputatorEvent> computatorEventStream;

    private EngineUnit currentUnit;

    @Setter
    private EngineListener engineListener;

    /**
     * Initialize new instance of {@link Engine}
     */
    @Inject
    public Engine(SceneProxy sceneProxy,
                  AsyncTaskManager asyncTaskManager,
                  ActionProducer actionProducer,
                  EngineEventStream<ComputatorEvent> computatorEventStream) {
        this.asyncTaskManager = asyncTaskManager;
        this.actionProducer = actionProducer;

        this.sceneProxy = sceneProxy;
        this.sceneProxy.setStateListener(this::onSceneFocusChanged);

        this.computatorEventStream = computatorEventStream;
        this.computatorEventStream.setListener(this::handleComputatorEvent);
        this.computatorEventStream.start();
    }

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStage(FrameStageData data) {
        this.currentUnit = new EngineUnit(data, this.asyncTaskManager, this.computatorEventStream);
        this.actionProducer.setListener(this.currentUnit::onActionPerform);
        this.sceneProxy.renderFrameStage(this.currentUnit.getFrameStage());
        this.sceneProxy.showTitle(data.getMapDisplayName());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.sceneProxy.sceneStart();
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

        if (this.currentUnit == null) {
            log.info("Current unit is null. No event will be handled.");
            return;
        }

        switch (event.getComputatorEventType()) {
            case ACTING_CHARACTER_JUMP:
                this.actionProducer.setListener(null);
                JumpingEngineModel jumpingEngineModel = this.currentUnit.findJumpingPoint(
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
        if (this.currentUnit == null) {
            return;
        }
        this.currentUnit.setPauseComputation(!sceneHasFocus);
    }
}
