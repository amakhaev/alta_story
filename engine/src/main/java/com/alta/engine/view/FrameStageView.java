package com.alta.engine.view;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.service.stage.StageComputatorImpl;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.customException.EngineException;
import com.alta.engine.core.storage.EngineStorage;
import com.alta.engine.data.frameStage.FacilityEngineModel;
import com.alta.engine.data.frameStage.NpcEngineModel;
import com.alta.engine.view.componentProvider.ComputatorFrameStageProvider;
import com.alta.engine.view.componentProvider.FrameStageComponentProvider;
import com.alta.engine.view.components.frameStage.FrameStageComponent;
import com.alta.eventStream.EventProducer;
import com.google.common.base.Strings;
import com.google.inject.assistedinject.AssistedInject;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.awt.*;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Provides the dispatcher of computator
 */
@Slf4j
public class FrameStageView {

    private final StageComputatorImpl stageComputatorImpl;
    private final EngineStorage engineStorage;

    @Getter
    private final FrameStageComponent frameStage;

    /**
     * Initialize new instance of {@link FrameStageView}
     */
    @AssistedInject
    public FrameStageView(AsyncTaskManager asyncTaskManager,
                          @Named("computatorActionProducer") EventProducer<ComputatorEvent> computatorEventProducer,
                          EngineStorage engineStorage) {
        this.engineStorage = engineStorage;
        try {
            this.stageComputatorImpl = ComputatorFrameStageProvider.createStageComputator(
                    this.engineStorage.getFrameStageData().getFocusPointMapStartPosition(),
                    this.engineStorage.getFrameStageData().getActingCharacter(),
                    this.engineStorage.getFrameStageData().getFacilities().stream().filter(FacilityEngineModel::isVisible).collect(Collectors.toList()),
                    this.engineStorage.getFrameStageData().getNpcList(),
                    computatorEventProducer
            );

            this.frameStage = FrameStageComponentProvider.createFrameStage(
                    this.engineStorage.getFrameStageData(), this.stageComputatorImpl, asyncTaskManager
            );
        } catch (Exception e) {
            throw new EngineException(e);
        }
    }

    /**
     * Performs the movement of focus point on scene
     *
     * @param movementDirection - the direction of movement to be performed.
     */
    public void onMovementPerform(MovementDirection movementDirection) {
        this.stageComputatorImpl.tryToRunMovement(movementDirection);
    }

    /**
     * Performs the movement of NPC on scene.
     *
     * @param npcTargetUuid - the NPC uuid.
     * @param x             - the X coordinate to be moved.
     * @param y             - the Y coordinate to be moved.
     */
    public void onMovementPerform(String npcTargetUuid, int x, int y) {
        this.stageComputatorImpl.tryToRunNpcMovement(npcTargetUuid, x, y);
    }

    /**
     * Gets the map coordinates of acting character.
     */
    public Point getActingCharacterMapCoordinate() {
        return this.stageComputatorImpl.getActorParticipant(
                this.engineStorage.getFrameStageData().getActingCharacter().getUuid()
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
                this.engineStorage.getFrameStageData().getActingCharacter().getUuid()
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
     * Finds the participant that targeted by acting character.
     *
     * @return the {@link TargetedParticipantSummary} instance or null if not found.
     */
    public TargetedParticipantSummary findParticipantTargetedByActingCharacter() {
        return this.stageComputatorImpl.findParticipantTargetedByActingCharacter();
    }

    /**
     * Finds the NPC by given uuid.
     *
     * @param uuid - the UUID of NPC to be found.
     * @return found {@link NpcEngineModel} or null.
     */
    public NpcEngineModel findNpcByUuid(@NonNull String uuid) {
        return this.engineStorage.getFrameStageData().getNpcList().stream()
                .filter(npc -> npc.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds the facility to map.
     *
     * @param facilityUuid - the uuid of facility to be added.
     */
    public void addFacility(@NonNull String facilityUuid) {
        FacilityEngineModel facilityEngineModel = this.engineStorage.getFrameStageData().getFacilities().stream()
                .filter(facility -> facility.getUuid().equals(facilityUuid))
                .findFirst()
                .orElse(null);

        if (facilityEngineModel == null) {
            log.error("Facility for given UUID {} not found. It must be described in map.dscr file.", facilityUuid);
            return;
        }

        this.stageComputatorImpl.addFacilities(
                Collections.singletonList(ComputatorFrameStageProvider.createFacilityParticipant(facilityEngineModel))
        );
        this.frameStage.addFacilityComponent(FrameStageComponentProvider.createFacilityComponent(facilityEngineModel));
    }

    /**
     * Removes the facility from frame stage and computator.
     *
     * @param facilityUuid - the uuid of facility to be removed.
     */
    public void removeFacility(@NonNull String facilityUuid) {
        this.frameStage.removeFacilityComponent(facilityUuid);
        this.stageComputatorImpl.removeFacility(facilityUuid);
    }

}
