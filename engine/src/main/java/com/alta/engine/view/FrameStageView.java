package com.alta.engine.view;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.stage.StageComputatorImpl;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.customException.EngineException;
import com.alta.engine.model.JumpingEngineModel;
import com.alta.engine.model.SimpleNpcEngineModel;
import com.alta.engine.utils.dataBuilder.ComputatorFrameStageProvider;
import com.alta.engine.utils.dataBuilder.FrameStageData;
import com.alta.engine.utils.dataBuilder.SceneFrameStageProvider;
import com.alta.engine.view.components.frameStage.FrameStageComponent;
import com.alta.eventStream.EventProducer;
import com.google.common.base.Strings;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.awt.*;

/**
 * Provides the dispatcher of computator
 */
@Slf4j
public class FrameStageView {

    private final StageComputatorImpl stageComputatorImpl;

    @Getter
    private final FrameStageData frameStageData;

    @Getter
    private final FrameStageComponent frameStage;

    /**
     * Initialize new instance of {@link FrameStageView}
     */
    @AssistedInject
    public FrameStageView(@Assisted FrameStageData data,
                          AsyncTaskManager asyncTaskManager,
                          @Named("computatorActionProducer") EventProducer<ComputatorEvent> computatorEventProducer) {
        try {
            this.frameStageData = data;
            this.stageComputatorImpl = ComputatorFrameStageProvider.builder()
                    .focusPointStartPosition(data.getFocusPointMapStartPosition())
                    .actingCharacter(data.getActingCharacter())
                    .facilityModels(data.getFacilities())
                    .simpleNpc(data.getSimpleNpc())
                    .eventProducer(computatorEventProducer)
                    .build();

            this.frameStage = SceneFrameStageProvider.builder()
                    .data(data)
                    .stageComputator(this.stageComputatorImpl)
                    .asyncTaskManager(asyncTaskManager)
                    .build();
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
        this.stageComputatorImpl.tryToRunMovement(movementDirection);
    }

    /**
     * Gets the map coordinates of acting character.
     */
    public Point getActingCharacterMapCoordinate() {
        return this.stageComputatorImpl.getActorParticipant(
                this.frameStageData.getActingCharacter().getUuid()
        ).getCurrentMapCoordinates();
    }

    /**
     * Sets the pause for computator
     *
     * @param isPause - indicates when pause is enabled.
     */
    public void setPauseComputation(boolean isPause) {
        this.stageComputatorImpl.setPause(isPause);
    }

    /**
     * Sets the pause for computator
     *
     * @param isPause - indicates when pause is enabled.
     * @param uuid - the uuid of NPC to be paused
     */
    public void setPauseComputationForSimpleNpc(boolean isPause, String uuid) {
        this.stageComputatorImpl.setPause(isPause, uuid);
    }

    /**
     * Sets the direction of NPC depends on position of acting character. In fact NPC just changed direction to
     * acting character.
     *
     * @param uuid - the UUID of NPC.
     */
    public void setNpcDirectionTargetedToActingCharacter(String uuid) {
        if (Strings.isNullOrEmpty(uuid)) {
            return;
        }

        ActorParticipant npcCharacter = this.stageComputatorImpl.getActorParticipant(uuid);
        ActorParticipant actingCharacter = this.stageComputatorImpl.getActorParticipant(
                this.frameStageData.getActingCharacter().getUuid()
        );

        if (actingCharacter == null || npcCharacter == null) {
            return;
        }

        if (actingCharacter.getCurrentMapCoordinates().x < npcCharacter.getCurrentMapCoordinates().x) {
            npcCharacter.setCurrentDirection(MovementDirection.LEFT);
        } else if (actingCharacter.getCurrentMapCoordinates().x > npcCharacter.getCurrentMapCoordinates().x) {
            npcCharacter.setCurrentDirection(MovementDirection.RIGHT);
        } else if (actingCharacter.getCurrentMapCoordinates().y < npcCharacter.getCurrentMapCoordinates().y) {
            npcCharacter.setCurrentDirection(MovementDirection.UP);
        } else {
            npcCharacter.setCurrentDirection(MovementDirection.DOWN);
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

        JumpingEngineModel jumpingPoint = this.frameStageData.getJumpingPoints().stream()
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
        if (this.stageComputatorImpl == null || this.frameStageData == null) {
            return null;
        }

        ActorParticipant actingCharacter = this.stageComputatorImpl.getActorParticipant(
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

        CoordinatedParticipant character = this.stageComputatorImpl.findCharacterByPosition(targetCharacterMapCoordinate);
        if (character == null || Strings.isNullOrEmpty(character.getUuid())) {
            return null;
        }

        return this.frameStageData.getSimpleNpc().stream()
                .filter(npcModel -> npcModel.getUuid().equals(character.getUuid()))
                .findFirst()
                .orElse(null);
    }

}
