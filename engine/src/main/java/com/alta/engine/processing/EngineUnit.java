package com.alta.engine.processing;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.customException.EngineException;
import com.alta.engine.core.engieEventStream.EngineEventStream;
import com.alta.engine.data.JumpingEngineModel;
import com.alta.engine.processing.listener.sceneInput.SceneAction;
import com.alta.engine.processing.dataBuilder.ComputatorFrameStageProvider;
import com.alta.engine.processing.dataBuilder.FrameStageData;
import com.alta.engine.processing.dataBuilder.SceneFrameStageProvider;
import com.alta.engine.processing.sceneComponent.frameStage.FrameStageComponent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;

/**
 * Provides the dispatcher of computator
 */
@Slf4j
public class EngineUnit {

    private final StageComputator stageComputator;
    private final List<JumpingEngineModel> jumpigPoints;

    @Getter
    private final FrameStageComponent frameStage;

    /**
     * Initialize new instance of {@link EngineUnit}
     */
    public EngineUnit(FrameStageData data,
                      AsyncTaskManager asyncTaskManager,
                      EngineEventStream<ComputatorEvent> computatorEventStream) {
        try {
            this.stageComputator = ComputatorFrameStageProvider.builder()
                    .focusPointStartPosition(data.getFocusPointMapStartPosition())
                    .actingCharacter(data.getActingCharacter())
                    .facilityModels(data.getFacilities())
                    .simpleNpc(data.getSimpleNpc())
                    .eventStream(computatorEventStream)
                    .build();

            this.frameStage = SceneFrameStageProvider.builder()
                    .data(data)
                    .stageComputator(this.stageComputator)
                    .asyncTaskManager(asyncTaskManager)
                    .build();
            this.jumpigPoints = data.getJumpingPoints();
        } catch (Exception e) {
            throw new EngineException(e);
        }
    }

    /**
     * Performs the action on scene
     *
     * @param action - the action that should be performed
     */
    public void onActionPerform(SceneAction action) {
        switch (action) {
            case MOVE_UP:
                this.stageComputator.tryToRunMovement(MovementDirection.UP);
                break;
            case MOVE_DOWN:
                this.stageComputator.tryToRunMovement(MovementDirection.DOWN);
                break;
            case MOVE_LEFT:
                this.stageComputator.tryToRunMovement(MovementDirection.LEFT);
                break;
            case MOVE_RIGHT:
                this.stageComputator.tryToRunMovement(MovementDirection.RIGHT);
                break;
        }
    }

    /**
     * Finds the jumping point from existing one.
     *
     * @param mapCoordinates - the jump point to be find.
     * @return found {@link JumpingEngineModel} point.
     */
    public JumpingEngineModel findJumpingPoint(Point mapCoordinates) {
        if (mapCoordinates == null) {
            log.error("The coordinates of map to be jumped is null");
            return null;
        }

        JumpingEngineModel jumpingPoint = this.jumpigPoints.stream()
                .filter(jp -> jp.getFrom().x == mapCoordinates.x && jp.getFrom().y == mapCoordinates.y)
                .findAny()
                .orElse(null);

        if (jumpingPoint == null) {
            log.warn("Jump point for {} not found.", mapCoordinates);
        } else {
            log.info("Jump point for given coordinates {} found, map name: {}.", mapCoordinates, jumpingPoint.getMapName());
        }

        return jumpingPoint;
    }
}
