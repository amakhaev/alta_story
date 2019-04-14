package com.alta.engine.facade;

import com.alta.computator.model.event.ActingCharacterJumpEvent;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.engine.eventProducer.EngineEvent;
import com.alta.engine.eventProducer.EngineEventType;
import com.alta.engine.eventProducer.eventPayload.JumpingEventPayload;
import com.alta.engine.eventProducer.eventPayload.SaveStateEventPayload;
import com.alta.engine.model.FrameStageDataModel;
import com.alta.engine.model.frameStage.JumpingEngineModel;
import com.alta.eventStream.EventProducer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.awt.*;

/**
 * Provides the listener of events that happens on frame stage
 */
@Slf4j
@Singleton
public class FrameStageListener {

    private final EventProducer<EngineEvent> engineEventProducer;
    private final FrameStageFacade frameStageFacade;

    /**
     * Initialize new instance of {@link FrameStageListener}.
     */
    @Inject
    public FrameStageListener(@Named("engineEventProducer") EventProducer<EngineEvent> engineEventProducer,
                              FrameStageFacade frameStageFacade) {
        this.engineEventProducer = engineEventProducer;
        this.frameStageFacade = frameStageFacade;
    }

    /**
     * Handles the save event from frame stage.
     */
    public void handleSaveEvent() {
        FrameStageDataModel frameStageDataModel = this.frameStageFacade.getFrameStageDataModel();
        Point actionCharacterMapCoordinates = this.frameStageFacade.getActingCharacterMapCoordinate();

        if (frameStageDataModel == null || actionCharacterMapCoordinates == null) {
            return;
        }

        this.engineEventProducer.publishEvent(
                new EngineEvent(EngineEventType.SAVE_STATE, new SaveStateEventPayload(
                        frameStageDataModel.getMapName(),
                        frameStageDataModel.getActingCharacter().getSkinName(),
                        actionCharacterMapCoordinates
                ))
        );
    }

    /**
     * Handles the events that have got from computator.
     *
     * @param event - the event that happens in computator.
     */
    public void handleComputatorEvent(ComputatorEvent event) {
        if (event == null) {
            log.error("Computator event is null.");
            return;
        }

        if (this.engineEventProducer == null) {
            log.info("No producer of engine event. No event will be handled.");
            return;
        }

        switch (event.getComputatorEventType()) {
            case ACTING_CHARACTER_JUMP:
                JumpingEngineModel jumpingEngineModel = this.frameStageFacade.findJumpingPoint(
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

}
