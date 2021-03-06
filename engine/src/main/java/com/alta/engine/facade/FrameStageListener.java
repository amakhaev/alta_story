package com.alta.engine.facade;

import com.alta.behaviorprocess.controller.globalEvent.GlobalEventController;
import com.alta.behaviorprocess.controller.localMap.LocalMapController;
import com.alta.computator.model.event.ActingCharacterJumpEvent;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.engine.core.storage.EngineStorage;
import com.alta.engine.data.FrameStageEngineDataModel;
import com.alta.engine.data.frameStage.JumpingEngineModel;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the listener of events that happens on frame stage
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrameStageListener {

    private final FrameStageFacade frameStageFacade;
    private final EngineStorage engineStorage;
    private final LocalMapController localMapController;
    private final GlobalEventController globalEventController;

    /**
     * Handles the save event from frame stage.
     */
    public void handleSaveEvent() {
        FrameStageEngineDataModel frameStageEngineDataModel = this.engineStorage.getFrameStageData();
        Point actionCharacterMapCoordinates = this.frameStageFacade.getActingCharacterMapCoordinate();

        if (frameStageEngineDataModel == null || actionCharacterMapCoordinates == null) {
            return;
        }

        this.globalEventController.saveGameState(
                frameStageEngineDataModel.getMapName(),
                frameStageEngineDataModel.getActingCharacter().getSkinName(),
                actionCharacterMapCoordinates
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
                    this.localMapController.jumpToMap(jumpingEngineModel.getMapName(), jumpingEngineModel.getTo());
                }
                break;
            default:
                log.error("Unknown type of computator event {}", event.getComputatorEventType());
        }
    }

}
