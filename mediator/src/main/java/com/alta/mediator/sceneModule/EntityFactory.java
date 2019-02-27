package com.alta.mediator.sceneModule;

import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.service.movement.StageComputator;
import com.alta.dao.data.map.MapFacilityModel;
import com.alta.dao.data.map.MapModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.sceneModule.entities.BaseFacility;
import com.alta.mediator.sceneModule.entities.BaseFrameStage;
import com.alta.mediator.sceneModule.entities.BaseFrameTemplate;
import com.alta.mediator.sceneModule.inputManagement.ActionProducer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the factory to generate entities
 */
@Singleton
public class EntityFactory {

    private final PreservationService preservationService;
    private final MapService mapService;
    private final ActionProducer actionProducer;

    /**
     * Initialize new instance of {@link EntityFactory}
     */
    @Inject
    public EntityFactory(PreservationService preservationService,
                         MapService mapService,
                         ActionProducer actionProducer) {
        this.preservationService = preservationService;
        this.mapService = mapService;
        this.actionProducer = actionProducer;
    }

    /**
     * Creates the frame stage from preservation
     *
     * @return created {@link BaseFrameStage}
     */
    public BaseFrameStage createFrameStageFromPreservation() {
        PreservationModel preservation = this.preservationService.getPreservation();
        MapModel mapFromPreservation = this.mapService.getMap(preservation.getMapName());

        return new BaseFrameStage(
                new BaseFrameTemplate(mapFromPreservation.getTiledMapAbsolutePath()),
                Collections.emptyList(),
                this.createSceneFacilities(mapFromPreservation.getFacilities()),
                this.createStageComputator(
                        new Point(preservation.getFocusX(), preservation.getFocusY()),
                        mapFromPreservation.getFacilities()
                ),
                this.actionProducer
        );
    }

    private StageComputator createStageComputator(Point focusPointStartPosition, List<MapFacilityModel> facilityModels) {
        StageComputator stageComputator = new StageComputator();
        stageComputator.addFocusPointParticipant(focusPointStartPosition);
        stageComputator.addFacilities(this.createComputeFacilities(facilityModels));

        return stageComputator;
    }

    private List<BaseFacility> createSceneFacilities(List<MapFacilityModel> facilityModels) {
        if (facilityModels == null) {
            return Collections.emptyList();
        }

        return facilityModels.parallelStream()
                .map(facilityModel -> new BaseFacility(
                        facilityModel.getUuid(),
                        facilityModel.getPathToImageSet(),
                        facilityModel.getTileWidth(),
                        facilityModel.getTileHeight())
                )
                .collect(Collectors.toList());
    }

    private List<CoordinatedParticipant> createComputeFacilities(List<MapFacilityModel> facilityModels) {
        if (facilityModels == null || facilityModels.isEmpty()) {
            return Collections.emptyList();
        }

        return facilityModels.parallelStream()
                .map(facilityModel -> new CoordinatedParticipant(
                        facilityModel.getUuid().toString(),
                        new Point(facilityModel.getStartX(), facilityModel.getStartY()),
                        0
                        )
                )
                .collect(Collectors.toList());
    }
}
