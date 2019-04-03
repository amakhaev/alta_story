package com.alta.engine.view;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.customException.EngineException;
import com.alta.engine.core.engineEventStream.EngineEventStream;
import com.alta.engine.model.JumpingEngineModel;
import com.alta.engine.model.SimpleNpcEngineModel;
import com.alta.engine.utils.dataBuilder.ComputatorFrameStageProvider;
import com.alta.engine.utils.dataBuilder.FrameStageData;
import com.alta.engine.utils.dataBuilder.SceneFrameStageProvider;
import com.alta.engine.view.components.frameStage.FrameStageComponent;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;

/**
 * Provides the dispatcher of computator
 */
@Slf4j
public class FrameStageView {

    private final StageComputator stageComputator;
    private final List<JumpingEngineModel> jumpigPoints;
    private final FrameStageData frameStageData;

    @Getter
    private final FrameStageComponent frameStage;

    /**
     * Initialize new instance of {@link FrameStageView}
     */
    public FrameStageView(FrameStageData data,
                          AsyncTaskManager asyncTaskManager,
                          EngineEventStream<ComputatorEvent> computatorEventStream) {
        try {
            this.frameStageData = data;
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
     * Performs the movement on scene
     *
     * @param movementDirection - the movement that should be performed
     */
    public void onMovementPerform(MovementDirection movementDirection) {
        this.stageComputator.tryToRunMovement(movementDirection);
    }

    /**
     * Sets the pause for computator
     *
     * @param isPause - indicates when pause is enabled.
     */
    public void setPauseComputation(boolean isPause) {
        this.stageComputator.setPause(isPause);
    }

    /**
     * Sets the pause for computator
     *
     * @param isPause - indicates when pause is enabled.
     * @param uuid - the uuid of NPC to be paused
     */
    public void setPauseComputationForSimpleNpc(boolean isPause, String uuid) {
        this.stageComputator.setPause(isPause, uuid);
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

    /**
     * Finds the simple npc that targeted by acting character.
     *
     * @return the {@link SimpleNpcEngineModel} instance of ull if not found.
     */
    public SimpleNpcEngineModel findSimpleNpcTargetedByActingCharacter() {
        if (this.stageComputator == null || this.frameStageData == null) {
            return null;
        }

        ActorParticipant actingCharacter = this.stageComputator.getActorParticipant(
                this.frameStageData.getActingCharacter().getUuid()
        );

        if (actingCharacter == null || actingCharacter.getCurrentMapCoordinates() == null) {
            return null;
        }

        Point targetCharacterMapCoordinate = new Point(actingCharacter.getCurrentMapCoordinates());
        switch (actingCharacter.getCurrentDirection()) {
            case UP:
                targetCharacterMapCoordinate.y--;
                break;
            case DOWN:
                targetCharacterMapCoordinate.y++;
                break;
            case LEFT:
                targetCharacterMapCoordinate.x--;
                break;
            case RIGHT:
                targetCharacterMapCoordinate.x++;
                break;
        }

        String characterUuid = this.stageComputator.findCharacterIdByPosition(targetCharacterMapCoordinate);
        if (Strings.isNullOrEmpty(characterUuid)) {
            return null;
        }

        return this.frameStageData.getSimpleNpc().stream()
                .filter(npcModel -> npcModel.getUuid().equals(characterUuid))
                .findFirst()
                .orElse(null);
    }
}
