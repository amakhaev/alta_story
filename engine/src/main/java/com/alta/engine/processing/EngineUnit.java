package com.alta.engine.processing;

import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.customException.EngineException;
import com.alta.engine.core.inputListener.ActionProducer;
import com.alta.engine.core.inputListener.SceneAction;
import com.alta.engine.processing.dataBuilder.ComputatorFrameStageProvider;
import com.alta.engine.processing.dataBuilder.FrameStageData;
import com.alta.engine.processing.dataBuilder.SceneFrameStageProvider;
import com.alta.engine.processing.sceneComponent.frameStage.FrameStageComponent;
import lombok.Getter;

/**
 * Provides the dispatcher of computator
 */
public class EngineUnit {

    @Getter
    private final StageComputator stageComputator;

    @Getter
    private final FrameStageComponent frameStage;

    /**
     * Initialize new instance of {@link EngineUnit}
     */
    public EngineUnit(FrameStageData data, ActionProducer actionProducer, AsyncTaskManager asyncTaskManager) {
        try {
            this.stageComputator = ComputatorFrameStageProvider.builder()
                    .focusPointStartPosition(data.getFocusPointMapStartPosition())
                    .actingCharacter(data.getActingCharacter())
                    .facilityModels(data.getFacilities())
                    .simpleNpc(data.getSimpleNpc())
                    .build();



            this.frameStage = SceneFrameStageProvider.builder()
                    .data(data)
                    .stageComputator(this.stageComputator)
                    .asyncTaskManager(asyncTaskManager)
                    .build();
            actionProducer.setListener(this::onActionPerform);
        } catch (Exception e) {
            throw new EngineException(e);
        }
    }

    private void onActionPerform(SceneAction action) {
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
}
