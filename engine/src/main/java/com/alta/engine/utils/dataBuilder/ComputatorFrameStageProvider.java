package com.alta.engine.utils.dataBuilder;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.service.stage.StageComputatorImpl;
import com.alta.engine.core.engineEventStream.EngineEventStream;
import com.alta.engine.model.ActingCharacterEngineModel;
import com.alta.engine.model.FacilityEngineModel;
import com.alta.engine.model.SimpleNpcEngineModel;
import lombok.Builder;
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
     * @param eventStream - the event stream related to computator
     * @return the {@link StageComputatorImpl} instance.
     */
    @Builder
    public static StageComputatorImpl createStageComputator(Point focusPointStartPosition,
                                                            ActingCharacterEngineModel actingCharacter,
                                                            List<FacilityEngineModel> facilityModels,
                                                            List<SimpleNpcEngineModel> simpleNpc,
                                                            EngineEventStream<ComputatorEvent> eventStream) {
        log.debug("Started creating FrameStageComputator");
        StageComputatorImpl stageComputatorImpl = new StageComputatorImpl();
        stageComputatorImpl.addFocusPointParticipant(focusPointStartPosition);

        stageComputatorImpl.addActingCharacter(
                actingCharacter.getUuid(),
                actingCharacter.getStartMapCoordinates(),
                actingCharacter.getZIndex()
        );

        if (facilityModels != null && !facilityModels.isEmpty()) {
            facilityModels.forEach(facilityModel ->
                    stageComputatorImpl.addFacilities(
                            facilityModel.getUuid().toString(),
                            createComputeFacilities(facilityModel),
                            new Point(facilityModel.getStartX(), facilityModel.getStartY())
                    )
            );
        }

        if (simpleNpc != null && !simpleNpc.isEmpty()) {
            addSimpleNpcInStageComputator(simpleNpc, stageComputatorImpl);
        }

        stageComputatorImpl.setComputatorEventProducer(eventStream);
        log.info("Creating of StageComputatorImpl completed.");
        return stageComputatorImpl;
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

    private static void addSimpleNpcInStageComputator(List<SimpleNpcEngineModel> engineModels, StageComputatorImpl stageComputatorImpl) {
        if (engineModels == null || engineModels.isEmpty()) {
            return;
        }

        engineModels.forEach(
                engineModel -> {
                    stageComputatorImpl.addSimpleNpcCharacter(
                            engineModel.getUuid(),
                            engineModel.getStartMapCoordinates(),
                            engineModel.getZIndex(),
                            engineModel.getRepeatingMovementDurationTime()
                    );
                }
        );
    }

}
