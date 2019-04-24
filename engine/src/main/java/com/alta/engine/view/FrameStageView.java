package com.alta.engine.view;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.stage.StageComputatorImpl;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.customException.EngineException;
import com.alta.engine.model.FrameStageDataModel;
import com.alta.engine.model.frameStage.FacilityEngineModel;
import com.alta.engine.view.componentProvider.ComputatorFrameStageProvider;
import com.alta.engine.view.componentProvider.FrameStageComponentProvider;
import com.alta.engine.view.components.frameStage.FrameStageComponent;
import com.alta.eventStream.EventProducer;
import com.google.common.base.Strings;
import com.google.inject.assistedinject.Assisted;
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
    private final FrameStageDataModel frameStageDataModel;

    @Getter
    private final FrameStageComponent frameStage;

    /**
     * Initialize new instance of {@link FrameStageView}
     */
    @AssistedInject
    public FrameStageView(@Assisted FrameStageDataModel data,
                          AsyncTaskManager asyncTaskManager,
                          @Named("computatorActionProducer") EventProducer<ComputatorEvent> computatorEventProducer) {
        try {
            this.frameStageDataModel = data;
            this.stageComputatorImpl = ComputatorFrameStageProvider.createStageComputator(
                    data.getFocusPointMapStartPosition(),
                    data.getActingCharacter(),
                    data.getFacilities().stream().filter(FacilityEngineModel::isVisible).collect(Collectors.toList()),
                    data.getSimpleNpc(),
                    computatorEventProducer
            );

            this.frameStage = FrameStageComponentProvider.createFrameStage(data, this.stageComputatorImpl, asyncTaskManager);
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
                this.frameStageDataModel.getActingCharacter().getUuid()
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
                this.frameStageDataModel.getActingCharacter().getUuid()
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
     * Adds the facility to map.
     *
     * @param facilityUuid - the uuid of facility to be added.
     */
    public void addFacility(@NonNull String facilityUuid) {
        FacilityEngineModel facilityEngineModel = this.frameStageDataModel.getFacilities().stream()
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
