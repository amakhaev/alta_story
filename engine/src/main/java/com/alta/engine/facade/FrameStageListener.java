package com.alta.engine.facade;

import com.alta.computator.model.event.ActingCharacterJumpEvent;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.engine.eventProducer.EngineEvent;
import com.alta.engine.eventProducer.EngineEventType;
import com.alta.engine.eventProducer.eventPayload.InteractionCompletedEventPayload;
import com.alta.engine.eventProducer.eventPayload.JumpingEventPayload;
import com.alta.engine.eventProducer.eventPayload.SaveStateEventPayload;
import com.alta.engine.data.FrameStageEngineDataModel;
import com.alta.engine.data.frameStage.JumpingEngineModel;
import com.alta.eventStream.EventProducer;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.awt.*;

/**
 * Provides the listener of events that happens on frame stage
 */
@Slf4j
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
        FrameStageEngineDataModel frameStageEngineDataModel = this.frameStageFacade.getFrameStageEngineDataModel();
        Point actionCharacterMapCoordinates = this.frameStageFacade.getActingCharacterMapCoordinate();

        if (frameStageEngineDataModel == null || actionCharacterMapCoordinates == null) {
            return;
        }

        this.engineEventProducer.publishEvent(
                new EngineEvent(EngineEventType.SAVE_STATE, new SaveStateEventPayload(
                        frameStageEngineDataModel.getMapName(),
                        frameStageEngineDataModel.getActingCharacter().getSkinName(),
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

        JumpingEngineModel jumpingEngineModel;
        switch (event.getComputatorEventType()) {
            case BEFORE_MOVING_FOCUS_TO_JUMP_POINT:
                jumpingEngineModel = this.frameStageFacade.findJumpingPoint(
                        ((ActingCharacterJumpEvent) event).getMapCoordinates()
                );
                if (jumpingEngineModel != null &&
                        !Strings.isNullOrEmpty(jumpingEngineModel.getHideFacilityUuid()) &&
                        !Strings.isNullOrEmpty(jumpingEngineModel.getShowFacilityUuid())) {
                    this.frameStageFacade.replaceFacility(jumpingEngineModel.getHideFacilityUuid(), jumpingEngineModel.getShowFacilityUuid());
                }
                break;
            case ACTING_CHARACTER_JUMP:
                jumpingEngineModel = this.frameStageFacade.findJumpingPoint(
                        ((ActingCharacterJumpEvent) event).getMapCoordinates()
                );
                if (jumpingEngineModel != null) {
                    this.engineEventProducer.publishEvent(new EngineEvent(
                            EngineEventType.JUMPING,
                            new JumpingEventPayload(jumpingEngineModel.getMapName(), jumpingEngineModel.getTo())
                    ));
                }
                break;
            default:
                log.error("Unknown type of computator event {}", event.getComputatorEventType());
                return;
        }
    }

}
