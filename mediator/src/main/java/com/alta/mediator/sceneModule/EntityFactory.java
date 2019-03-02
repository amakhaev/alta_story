package com.alta.mediator.sceneModule;

import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.service.stage.StageComputator;
import com.alta.dao.data.facility.FacilityTileType;
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

    private List<FacilityPartParticipant> createComputeFacilities(MapFacilityModel facilityModel) {
        if (facilityModel == null) {
            return Collections.emptyList();
        }

        return facilityModel.getFacilityPositions()
                .stream()
                .map(facilityPartPosition ->
                    new FacilityPartParticipant(
                            facilityModel.getUuid().toString(),
                            facilityPartPosition.getZIndex(),
                            new Point(facilityModel.getStartX(), facilityModel.getStartY()),
                            new Point(facilityPartPosition.getShiftFromStartX(), facilityPartPosition.getShiftFromStartY()),
                            new Point(facilityPartPosition.getX(), facilityPartPosition.getY()),
                            this.convertTileTypeToTileState(facilityPartPosition.getTileType())
                    )
                )
                .collect(Collectors.toList());
    }

    private TileState convertTileTypeToTileState(FacilityTileType type) {
        switch (type) {
            case FREE:
                return TileState.FREE;
            case BARRIER:
                return TileState.BARRIER;
            default:
                return TileState.FREE;
        }
    }
}
