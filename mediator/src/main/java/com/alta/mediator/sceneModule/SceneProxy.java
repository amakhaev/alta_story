package com.alta.mediator.sceneModule;

import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.map.MapsContainer;
import com.alta.dao.domain.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.scene.Scene;
import com.alta.scene.frameStorage.FrameTemplate;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Provides the proxy object for access to scene
 */
@Singleton
public class SceneProxy {

    private final MapsContainer mapsContainer;
    private final PreservationService preservationService;
    private final MapService mapService;
    private final Scene scene;

    /**
     * Initialize new instance of {@link SceneProxy}
     */
    @Inject
    public SceneProxy(MapsContainer mapsContainer,
                      PreservationService preservationService,
                      MapService mapService,
                      Scene scene) {
        this.mapsContainer = mapsContainer;

        this.preservationService = preservationService;
        this.mapService = mapService;
        this.scene = scene;
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
        FrameTemplate template = new FrameImpl(absolutePathToTiledMap);
        this.scene.renderStage(new FrameStageImpl(template));
    }

}
