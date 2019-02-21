package com.alta.mediator.sceneModule;

import com.alta.computator.service.movement.StageComputator;
import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.map.MapsContainer;
import com.alta.dao.domain.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.sceneModule.entities.BaseFrameStage;
import com.alta.mediator.sceneModule.entities.BaseFrameTemplate;
import com.alta.scene.Scene;
import com.alta.utils.ThreadPoolExecutor;
import com.google.inject.Inject;

import java.util.Collections;

/**
 * Provides the proxy object for access to scene
 */
public class SceneProxy {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final MapsContainer mapsContainer;
    private final PreservationService preservationService;
    private final MapService mapService;
    private final Scene scene;
    private final StageComputator stageComputator;

    /**
     * Initialize new instance of {@link SceneProxy}
     */
    @Inject
    public SceneProxy(ThreadPoolExecutor threadPoolExecutor,
                      MapsContainer mapsContainer,
                      PreservationService preservationService,
                      MapService mapService,
                      Scene scene,
                      StageComputator computator) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.mapsContainer = mapsContainer;

        this.preservationService = preservationService;
        this.mapService = mapService;
        this.scene = scene;
        this.stageComputator = computator;
    }

    /**
     * Starts the scene
     */
    public void sceneStart() {
        this.scene.start();
    }

    /**
     * Loads scene state from preservation
     */
    public void loadSceneFromPreservation() {
        PreservationModel preservation = this.preservationService.getPreservation();
        String absolutePathToTiledMap = this.mapService.getAbsolutePathToMap(
                this.mapsContainer.getMapByName(preservation.getMapName()).getPath()
        );
        this.scene.renderStage(
                new BaseFrameStage(
                        new BaseFrameTemplate(absolutePathToTiledMap),Collections.emptyList(),
                        this.threadPoolExecutor,
                        stageComputator)
        );
    }

}
