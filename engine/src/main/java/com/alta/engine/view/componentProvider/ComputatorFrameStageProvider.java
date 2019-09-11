package com.alta.engine.view.componentProvider;

import com.alta.computator.core.computator.movement.GlobalMovementCalculator;
import com.alta.computator.core.computator.movement.MovementType;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.core.computator.movement.directionCalculation.RouteMovementDescription;
import com.alta.computator.facade.dataWriter.DataWriterFacade;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.actor.RouteNpcParticipant;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.engine.data.frameStage.ActingCharacterEngineModel;
import com.alta.engine.data.frameStage.FacilityEngineModel;
import com.alta.engine.data.frameStage.NpcEngineModel;
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
     * Initializes the computator for initial values.
     *
     * @param dataWriterFacade - the {@link DataWriterFacade} instance.
     * @param focusPointStartPosition - the coordinates of focus point on map.
     * @param actingCharacter - the acting character model.
     * @param facilityModels - the facilities that available on map.
     */
    public void initializeComputator(DataWriterFacade dataWriterFacade,
                                     Point focusPointStartPosition,
                                     ActingCharacterEngineModel actingCharacter,
                                     List<FacilityEngineModel> facilityModels,
                                     List<NpcEngineModel> simpleNpc) {
        log.debug("Started initializing of computator");
        dataWriterFacade.addFocusPoint(focusPointStartPosition);
        dataWriterFacade.addFacilities(createFacilityParticipants(facilityModels));
        dataWriterFacade.addNpcCharacters(createNpcParticipants(simpleNpc));

        dataWriterFacade.addActingCharacter(
                new ActingCharacterParticipant(
                        actingCharacter.getUuid(), actingCharacter.getStartMapCoordinates(), actingCharacter.getZIndex()
                )
        );

        log.info("Computator initialization was completed.");
    }

    /**
     * Adds the facility participant into computator.
     *
     * @param facilityModel - the facility model from which created participant.
     */
    public void addFacilityParticipant(DataWriterFacade dataWriterFacade, @NonNull FacilityEngineModel facilityModel) {
        dataWriterFacade.addFacilities(
                Collections.singletonList(ComputatorFrameStageProvider.createFacilityParticipant(facilityModel))
        );
    }

    private FacilityParticipant createFacilityParticipant(@NonNull FacilityEngineModel facilityModel) {
        return new FacilityParticipant(
                facilityModel.getUuid(),
                new Point(facilityModel.getStartX(), facilityModel.getStartY()),
                createFacilityParts(facilityModel)
        );
    }

    private List<NpcParticipant> createNpcParticipants(List<NpcEngineModel> npcEngineModels) {
        if (npcEngineModels == null || npcEngineModels.isEmpty()) {
            return Collections.emptyList();
        }

        return npcEngineModels.stream()
                .map(npcEngineModel -> {
                    MovementType movementStrategy = null;
                    try {
                        movementStrategy = Strings.isNullOrEmpty(npcEngineModel.getMovementStrategy()) ?
                                MovementType.AVOID_OBSTRUCTION : MovementType.valueOf(npcEngineModel.getMovementStrategy());
                    } catch (Exception e) {
                        log.error(
                                "Can't get participantComputator strategy for simple npcMovement {}, given strategy {}",
                                npcEngineModel.getUuid(),
                                npcEngineModel.getMovementStrategy()
                        );
                        return null;
                    }

                    switch (movementStrategy) {
                        case AVOID_OBSTRUCTION:
                        case STAND_SPOT:
                            return createSimpleNpcParticipant(npcEngineModel, movementStrategy);
                        case ROUTE_POINTS:
                            return createRouteNpcParticipant(npcEngineModel);
                        default:
                            log.error("Unknown type of movement strategy for given NPC: {}", movementStrategy);
                            return null;
                    }
                })
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
                                facilityEngineModel.getUuid(),
                                facilityPartPosition.getZIndex(),
                                new Point(facilityEngineModel.getStartX(), facilityEngineModel.getStartY()),
                                new Point(facilityPartPosition.getShiftFromStartX(), facilityPartPosition.getShiftFromStartY()),
                                new Point(facilityPartPosition.getX(), facilityPartPosition.getY()),
                                facilityPartPosition.getTileState()
                        )
                )
                .collect(Collectors.toList());
    }

    private SimpleNpcParticipant createSimpleNpcParticipant(NpcEngineModel engineModel, MovementType movementType) {
        MovementDirection movementDirection = null;
        try {
            movementDirection = Strings.isNullOrEmpty(engineModel.getInitialDirection()) ?
                    MovementDirection.DOWN :
                    MovementDirection.valueOf(engineModel.getInitialDirection());
        } catch (Exception e) {
            log.error(
                    "Can't get initial direction for simple npcMovement {}, given direction {}",
                    engineModel.getUuid(),
                    engineModel.getInitialDirection()
            );
        }

        return new SimpleNpcParticipant(
                engineModel.getUuid(),
                engineModel.getStartMapCoordinates(),
                engineModel.getZIndex(),
                engineModel.getRepeatingMovementDurationTime(),
                movementType,
                movementDirection
        );
    }

    private RouteNpcParticipant createRouteNpcParticipant(NpcEngineModel engineModel) {
        MovementDirection movementDirection = null;
        try {
            movementDirection = Strings.isNullOrEmpty(engineModel.getInitialDirection()) ?
                    MovementDirection.DOWN :
                    MovementDirection.valueOf(engineModel.getInitialDirection());
        } catch (Exception e) {
            log.error(
                    "Can't get initial direction for simple npcMovement {}, given direction {}",
                    engineModel.getUuid(),
                    engineModel.getInitialDirection()
            );
        }

        RouteNpcParticipant routeNpcParticipant = new RouteNpcParticipant(
                engineModel.getUuid(),
                engineModel.getStartMapCoordinates(),
                engineModel.getZIndex(),
                engineModel.getRepeatingMovementDurationTime(),
                movementDirection,
                engineModel.isMovementRouteLooped(),
                createRouteMovementDescription(engineModel.getRouteDescription())
        );

        if (Strings.isNullOrEmpty(engineModel.getMovementSpeed())) {
            return routeNpcParticipant;
        }

        routeNpcParticipant.setMovementSpeed(GlobalMovementCalculator.determinateSpeed(engineModel.getMovementSpeed()));

        return routeNpcParticipant;
    }

    private List<RouteMovementDescription> createRouteMovementDescription(List<NpcEngineModel.RouteDescription> descriptions) {
        return descriptions.stream()
                .map(md -> new RouteMovementDescription(md.getX(), md.getY(), MovementDirection.valueOf(md.getFinalDirection())))
                .collect(Collectors.toList());
    }
}
