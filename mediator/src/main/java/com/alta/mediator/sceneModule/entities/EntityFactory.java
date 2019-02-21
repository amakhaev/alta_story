package com.alta.mediator.sceneModule.entities;

import com.alta.computator.service.movement.StageComputator;
import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.map.MapsContainer;
import com.alta.dao.domain.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.utils.ThreadPoolExecutor;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.awt.*;
import java.util.Collections;

/**
 * Provides the factory to generate entities
 */
@Singleton
public class EntityFactory {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final MapsContainer mapsContainer;
    private final PreservationService preservationService;
    private final MapService mapService;
    private final StageComputator stageComputator;

    /**
     * Initialize new instance of {@link EntityFactory}
     */
    @Inject
    public EntityFactory(ThreadPoolExecutor threadPoolExecutor,
                         MapsContainer mapsContainer,
                         PreservationService preservationService,
                         MapService mapService,
                         StageComputator computator) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.mapsContainer = mapsContainer;

        this.preservationService = preservationService;
        this.mapService = mapService;
        this.stageComputator = computator;
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

        this.stageComputator.addFocusPointParticipant(new Point(preservation.getFocusX(), preservation.getFocusY()));
        return new BaseFrameStage(
                new BaseFrameTemplate(absolutePathToTiledMap),
                Collections.emptyList(),
                this.stageComputator,
                this.threadPoolExecutor
        );
    }

}
