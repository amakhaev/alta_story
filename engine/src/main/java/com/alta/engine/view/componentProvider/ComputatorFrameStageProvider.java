package com.alta.engine.view.componentProvider;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.service.stage.StageComputatorImpl;
import com.alta.engine.model.frameStage.ActingCharacterEngineModel;
import com.alta.engine.model.frameStage.FacilityEngineModel;
import com.alta.engine.model.frameStage.SimpleNpcEngineModel;
import com.alta.eventStream.EventProducer;
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

    private List<SimpleNpcParticipant> createSimpleNpcParticipants(List<SimpleNpcEngineModel> simpleNpcEngineModels) {
        if (simpleNpcEngineModels == null || simpleNpcEngineModels.isEmpty()) {
            return Collections.emptyList();
        }

        return simpleNpcEngineModels.stream()
                .map(npc ->
                        new SimpleNpcParticipant(
                                npc.getUuid(),
                                npc.getStartMapCoordinates(),
                                npc.getZIndex(),
                                npc.getRepeatingMovementDurationTime()
                        )
                )
                .collect(Collectors.toList());
    }

    private List<FacilityParticipant> createFacilityParticipants(List<FacilityEngineModel> facilityModels) {
        if (facilityModels == null || facilityModels.isEmpty()) {
            return Collections.emptyList();
        }

        return facilityModels.stream()
                .map(facilityModel ->
                        new FacilityParticipant(
                                facilityModel.getUuid(),
                                new Point(facilityModel.getStartX(), facilityModel.getStartY()),
                                createFacilityParts(facilityModel)
                        )
                )
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
}
