package com.alta.engine.presenter;

import com.alta.computator.model.event.ActingCharacterJumpEvent;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.engine.eventProducer.EngineEvent;
import com.alta.engine.eventProducer.EngineEventType;
import com.alta.engine.eventProducer.eventPayload.JumpingEventPayload;
import com.alta.engine.model.JumpingEngineModel;
import com.alta.engine.model.SimpleNpcEngineModel;
import com.alta.engine.presenter.sceneProxy.SceneProxy;
import com.alta.engine.utils.dataBuilder.FrameStageData;
import com.alta.engine.view.FrameStageView;
import com.alta.engine.view.ViewFactory;
import com.alta.eventStream.EventProducer;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

/**
 * Provides the presenter of view.
 */
@Slf4j
@Singleton
public class FrameStagePresenter {

    private final SceneProxy sceneProxy;
    private final ViewFactory viewFactory;
    private final EventProducer<EngineEvent> engineEventProducer;

    private FrameStageView currentView;

    /**
     * Initialize new instance of {@link FrameStagePresenter}
     */
    @Inject
    public FrameStagePresenter(@Named("computatorActionProducer") EventProducer<ComputatorEvent> computatorActionProducer,
                               @Named("engineEventProducer") EventProducer<EngineEvent> engineEventProducer,
                               SceneProxy sceneProxy,
                               ViewFactory viewFactory) {

        this.sceneProxy = sceneProxy;
        this.viewFactory = viewFactory;
        this.engineEventProducer = engineEventProducer;
        this.sceneProxy.setStateListener(this::onSceneFocusChanged);

        computatorActionProducer.subscribe(this::handleComputatorEvent);
        computatorActionProducer.start();
    }

    /**
     * Loads scene state from characterPreservation
     */
    public void tryToRenderFrameStageView(FrameStageData data) {
        this.currentView = this.viewFactory.createFrameStageView(data);
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

    /**
     * Finds the simple npc that targeted by acting character.
     *
     * @return the {@link SimpleNpcEngineModel} instance of ull if not found.
     */
    public SimpleNpcEngineModel findSimpleNpcTargetedByActingCharacter() {
        return this.currentView == null ? null : this.currentView.findSimpleNpcTargetedByActingCharacter();
    }

    /**
     * Starts the interaction with NPC.
     *
     * @param uuid - the UUID of simple NPC to be interacted.
     */
    public void startInteractionWithNpc(String uuid) {
        if (Strings.isNullOrEmpty(uuid)) {
            return;
        }

        if (this.currentView != null) {
            this.currentView.setPauseComputationForSimpleNpc(true, uuid);
            this.currentView.setNpcDirectionTargetedToActingCharacter(uuid);
        }
    }

    /**
     * Stops the interaction with simple NPC.
     *
     * @param uuid - the UUID of simple NPC to be interacted.
     */
    public void stopInteractionWithNpc(String uuid) {
        if (Strings.isNullOrEmpty(uuid)) {
            return;
        }

        if (this.currentView != null) {
            this.currentView.setPauseComputationForSimpleNpc(false, uuid);
        }
    }

    private void handleComputatorEvent(ComputatorEvent event) {
        if (event == null) {
            log.error("Computator event is null.");
            return;
        }

        if (this.engineEventProducer == null) {
            log.info("No rpoducer of engine event. No event will be handled.");
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
                    this.engineEventProducer.publishEvent(new EngineEvent(
                            EngineEventType.JUMPING,
                            new JumpingEventPayload(jumpingEngineModel.getMapName(), jumpingEngineModel.getTo())
                    ));
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
