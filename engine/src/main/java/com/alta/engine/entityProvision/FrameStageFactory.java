package com.alta.engine.entityProvision;

import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.customException.EngineException;
import com.alta.engine.data.ActingCharacterModel;
import com.alta.engine.data.FacilityEngineModel;
import com.alta.engine.entityProvision.entities.BaseActingCharacter;
import com.alta.engine.entityProvision.entities.BaseFacility;
import com.alta.engine.entityProvision.entities.BaseFrameStage;
import com.alta.engine.entityProvision.entities.BaseFrameTemplate;
import com.alta.engine.inputListener.ActionProducer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the factory to generate entities
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrameStageFactory {

    private final ActionProducer actionProducer;

    /**
     * Creates the FrameStage instance by given data
     *
     * @param data - the data that full describes the frame stage
     * @return created {@link BaseFrameStage} instance based of @param data
     */
    public BaseFrameStage createFrameStage(FrameStageData data) throws EngineException {
        this.validateFrameStageData(data);

        StageComputator stageComputator = this.createStageComputator(
                data.getFocusPointMapStartPosition(),
                data.getActingCharacter(),
                data.getFacilities()
        );

        log.debug("Started creating BaseFrameStage with path to map: {}", data.getTiledMapAbsolutePath());
        BaseFrameStage baseFrameStage = new BaseFrameStage(
                new BaseFrameTemplate(data.getTiledMapAbsolutePath()),
                new BaseActingCharacter(data.getActingCharacter().getAnimationDescriptors()),
                this.createStageFacilities(data.getFacilities()),
                stageComputator,
                this.actionProducer
        );
        log.debug("Completed creating BaseFrameStage with map: {}", data.getTiledMapAbsolutePath());
        return baseFrameStage;
    }

    private StageComputator createStageComputator(Point focusPointStartPosition,
                                                  ActingCharacterModel actingCharacter,
                                                  List<FacilityEngineModel> facilityModels) {
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
                            this.createComputeFacilities(facilityModel),
                            new Point(facilityModel.getStartX(), facilityModel.getStartY())
                    )
            );
        }

        return stageComputator;
    }

    private List<BaseFacility> createStageFacilities(List<FacilityEngineModel> facilityEngineModels) {
        if (facilityEngineModels == null) {
            return Collections.emptyList();
        }

        return facilityEngineModels.parallelStream()
                .map(facilityModel -> new BaseFacility(
                        facilityModel.getUuid(),
                        facilityModel.getPathToImageSet(),
                        facilityModel.getTileWidth(),
                        facilityModel.getTileHeight())
                )
                .collect(Collectors.toList());
    }

    private List<FacilityPartParticipant> createComputeFacilities(FacilityEngineModel facilityEngineModel) {
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

    private void validateFrameStageData(FrameStageData data) throws EngineException {
        if (data == null) {
            throw new EngineException("The FrameStageData is null. It required for creating frame stage.");
        }

        if (data.getFocusPointMapStartPosition() == null) {
            throw new EngineException("The focus point is required for creating frame stage.");
        }

        if (data.getActingCharacter() == null) {
            throw new EngineException("The acting character not present for stage with path");
        }

        if (data.getActingCharacter().getAnimationDescriptors() == null ||
                data.getActingCharacter().getAnimationDescriptors().isEmpty()) {
            throw new EngineException("The acting character doesn't contains descriptors for animation");
        }
    }
}
