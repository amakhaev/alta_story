package com.alta.engine.view.componentProvider;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.movement.strategy.MovementStrategyFactory;
import com.alta.computator.service.stage.StageComputatorImpl;
import com.alta.engine.model.frameStage.ActingCharacterEngineModel;
import com.alta.engine.model.frameStage.FacilityEngineModel;
import com.alta.engine.model.frameStage.SimpleNpcEngineModel;
import com.alta.eventStream.EventProducer;
import com.google.common.base.Strings;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the factory for entities related to frame stage in computator
 */
@Slf4j
@UtilityClass
public class ComputatorFrameStageProvider {

    /**
     * Creates the frame stage computator
     *
     * @param focusPointStartPosition - the coordinates of focus point on map.
     * @param actingCharacter - the acting character model.
     * @param facilityModels - the facilities that available on map.
     * @param eventProducer - the event stream related to computator
     * @return the {@link StageComputatorImpl} instance.
     */
    public StageComputatorImpl createStageComputator(Point focusPointStartPosition,
                                                     ActingCharacterEngineModel actingCharacter,
                                                     List<FacilityEngineModel> facilityModels,
                                                     List<SimpleNpcEngineModel> simpleNpc,
                                                     EventProducer<ComputatorEvent> eventProducer) {
        log.debug("Started creating FrameStageComputator");
        StageComputatorImpl stageComputatorImpl = new StageComputatorImpl();
        stageComputatorImpl.addFocusPointParticipant(focusPointStartPosition);
        stageComputatorImpl.addFacilities(createFacilityParticipants(facilityModels));
        stageComputatorImpl.addSimpleNpcCharacters(createSimpleNpcParticipants(simpleNpc));

        stageComputatorImpl.addActingCharacter(
                new ActingCharacterParticipant(
                        actingCharacter.getUuid(),
                        actingCharacter.getStartMapCoordinates(),
                        actingCharacter.getZIndex()
                )
        );

        stageComputatorImpl.setComputatorEventProducer(eventProducer);
        log.info("Creating of StageComputatorImpl completed.");
        return stageComputatorImpl;
    }

    /**
     * Creates the facility participant for computations.
     *
     * @param facilityModel - the facility model from which created participant.
     * @return created {@link FacilityParticipant} instance.
     */
    public FacilityParticipant createFacilityParticipant(@NonNull FacilityEngineModel facilityModel) {
        return new FacilityParticipant(
                facilityModel.getUuid(),
                new Point(facilityModel.getStartX(), facilityModel.getStartY()),
                createFacilityParts(facilityModel)
        );
    }

    private List<SimpleNpcParticipant> createSimpleNpcParticipants(List<SimpleNpcEngineModel> simpleNpcEngineModels) {
        if (simpleNpcEngineModels == null || simpleNpcEngineModels.isEmpty()) {
            return Collections.emptyList();
        }

        return simpleNpcEngineModels.stream()
                .map(ComputatorFrameStageProvider::createSimpleNpcParticipant)
                .collect(Collectors.toList());
    }

    private List<FacilityParticipant> createFacilityParticipants(List<FacilityEngineModel> facilityModels) {
        if (facilityModels == null || facilityModels.isEmpty()) {
            return Collections.emptyList();
        }

        return facilityModels.stream()
                .map(ComputatorFrameStageProvider::createFacilityParticipant)
                .collect(Collectors.toList());
    }

    private List<FacilityPartParticipant> createFacilityParts(FacilityEngineModel facilityEngineModel) {
        if (facilityEngineModel == null ||
                facilityEngineModel.getPositions() == null ||
                facilityEngineModel.getPositions().isEmpty()) {
            return Collections.emptyList();
        }

        return facilityEngineModel.getPositions()
                .stream()
                .map(facilityPartPosition ->
                        new FacilityPartParticipant(
                                facilityEngineModel.getUuid().toString(),
                                facilityPartPosition.getZIndex(),
                                new Point(facilityEngineModel.getStartX(), facilityEngineModel.getStartY()),
                                new Point(facilityPartPosition.getShiftFromStartX(), facilityPartPosition.getShiftFromStartY()),
                                new Point(facilityPartPosition.getX(), facilityPartPosition.getY()),
                                facilityPartPosition.getTileState()
                        )
                )
                .collect(Collectors.toList());
    }

    private SimpleNpcParticipant createSimpleNpcParticipant(SimpleNpcEngineModel engineModel) {
        MovementStrategyFactory.Strategy movementStrategy = null;
        try {
            movementStrategy = Strings.isNullOrEmpty(engineModel.getMovementStrategy()) ?
                    MovementStrategyFactory.Strategy.AVOID_OBSTRUCTION :
                    MovementStrategyFactory.Strategy.valueOf(engineModel.getMovementStrategy());
        } catch (Exception e) {
            log.error(
                    "Can't get movement strategy for simple npc {}, given strategy {}",
                    engineModel.getUuid(),
                    engineModel.getMovementStrategy()
            );
            movementStrategy = MovementStrategyFactory.Strategy.AVOID_OBSTRUCTION;
        }

        MovementDirection movementDirection = null;
        try {
            movementDirection = Strings.isNullOrEmpty(engineModel.getInitialDirection()) ?
                    MovementDirection.DOWN :
                    MovementDirection.valueOf(engineModel.getInitialDirection());
        } catch (Exception e) {
            log.error(
                    "Can't get initial direction for simple npc {}, given direction {}",
                    engineModel.getUuid(),
                    engineModel.getInitialDirection()
            );
        }

        return new SimpleNpcParticipant(
                engineModel.getUuid(),
                engineModel.getStartMapCoordinates(),
                engineModel.getZIndex(),
                engineModel.getRepeatingMovementDurationTime(),
                movementStrategy,
                movementDirection
        );
    }
}
