package com.alta.mediator.sceneModule;

import com.alta.computator.service.movement.StageComputator;
import com.alta.dao.domain.facility.FacilityService;
import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.map.MapsContainer;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.sceneModule.entities.BaseFrameStage;
import com.alta.mediator.sceneModule.entities.BaseFrameTemplate;
import com.alta.mediator.sceneModule.inputManagement.ActionProducer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.awt.*;
import java.util.*;

/**
 * Provides the factory to generate entities
 */
@Singleton
public class EntityFactory {

    private final MapsContainer mapsContainer;
    private final PreservationService preservationService;
    private final MapService mapService;
    private final ActionProducer actionProducer;
    private final FacilityService facilityService;

    /**
     * Initialize new instance of {@link EntityFactory}
     */
    @Inject
    public EntityFactory(MapsContainer mapsContainer,
                         PreservationService preservationService,
                         MapService mapService, ActionProducer actionProducer,
                         FacilityService facilityService) {
        this.mapsContainer = mapsContainer;
        this.preservationService = preservationService;
        this.mapService = mapService;
        this.actionProducer = actionProducer;
        this.facilityService = facilityService;
    }

    /**
     * Creates the frame stage from preservation
     *
     * @return created {@link BaseFrameStage}
     */
    public BaseFrameStage createFrameStageFromPreservation() {
        PreservationModel preservation = this.preservationService.getPreservation();
        String absolutePathToTiledMap = this.mapService.getAbsolutePathToMap(
                this.mapsContainer.getMapByName(preservation.getMapName()).getPath()
        );

        return new BaseFrameStage(
                new BaseFrameTemplate(absolutePathToTiledMap),
                Collections.emptyList(),
                this.createStageComputator(new Point(preservation.getFocusX(), preservation.getFocusY())),
                this.actionProducer
        );
    }

    private StageComputator createStageComputator(Point focusPointStartPosition) {
        StageComputator stageComputator = new StageComputator();
        stageComputator.addFocusPointParticipant(focusPointStartPosition);

        /*Map<String, List<String>> f = new HashMap<>();
        f.put("facility1", Arrays.asList("cross2", "pedestal1", "statue3", "statue9", "statue24"));
        this.facilityService.findFacilitiesByName(f);*/

        return stageComputator;
    }
}
