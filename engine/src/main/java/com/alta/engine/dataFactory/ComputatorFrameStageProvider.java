package com.alta.engine.dataFactory;

import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.sceneComponent.actor.ActingCharacterEngineModel;
import com.alta.engine.sceneComponent.facility.FacilityEngineModel;
import com.alta.engine.sceneComponent.actor.SimpleNpcEngineModel;
import com.google.inject.Singleton;
import lombok.Builder;
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
public class ComputatorFrameStageProvider {

    /**
     * Creates the frame stage computator
     *
     * @param focusPointStartPosition - the coordinates of focus point on map.
     * @param actingCharacter - the acting character model.
     * @param facilityModels - the facilities that available on map.
     * @return the {@link StageComputator} instance.
     */
    @Builder
    public static StageComputator createStageComputator(Point focusPointStartPosition,
                                                        ActingCharacterEngineModel actingCharacter,
                                                        List<FacilityEngineModel> facilityModels,
                                                        List<SimpleNpcEngineModel> simpleNpc) {
        log.debug("Started creating FrameStageComputator");
        StageComputator stageComputator = new StageComputator();
        stageComputator.addFocusPointParticipant(focusPointStartPosition);

        stageComputator.addActingCharacter(
                actingCharacter.getUuid(),
                actingCharacter.getStartMapCoordinates(),
                actingCharacter.getZIndex()
        );

        if (facilityModels != null && !facilityModels.isEmpty()) {
            facilityModels.forEach(facilityModel ->
                    stageComputator.addFacilities(
                            facilityModel.getUuid().toString(),
                            createComputeFacilities(facilityModel),
                            new Point(facilityModel.getStartX(), facilityModel.getStartY())
                    )
            );
        }

        if (simpleNpc != null && !simpleNpc.isEmpty()) {
            addSimpleNpcInStageComputator(simpleNpc, stageComputator);
        }

        log.info("Creating of StageComputator completed.");
        return stageComputator;
    }

    private static List<FacilityPartParticipant> createComputeFacilities(FacilityEngineModel facilityEngineModel) {
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

    private static void addSimpleNpcInStageComputator(List<SimpleNpcEngineModel> engineModels, StageComputator stageComputator) {
        if (engineModels == null || engineModels.isEmpty()) {
            return;
        }

        engineModels.forEach(
                engineModel -> {
                    stageComputator.addSimpleNpcCharacter(
                            engineModel.getUuid(),
                            engineModel.getStartMapCoordinates(),
                            engineModel.getZIndex(),
                            engineModel.getRepeatingMovementDurationTime()
                    );
                }
        );
    }

}
