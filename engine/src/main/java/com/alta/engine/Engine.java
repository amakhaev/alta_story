package com.alta.engine;

import com.alta.engine.configuration.EngineConfiguration;
import com.alta.engine.facade.FrameStageFacade;
import com.alta.engine.model.FrameStageDataModel;
import com.alta.engine.model.InteractionDataModel;
import com.alta.interaction.interactionOnMap.InteractionOnMapManager;
import com.google.inject.Inject;

/**
 * Provides the engine that contains logic related to scene and calculation
 */
public class Engine {

    private final FrameStageFacade frameStageFacade;
    private final InteractionOnMapManager interactionOnMapManager;

    /**
     * Initialize new instance of {@link Engine}
     */
    @Inject
    public Engine(FrameStageFacade frameStageFacade,
                  EngineConfiguration engineConfiguration,
                  InteractionOnMapManager interactionOnMapManager) {
        this.frameStageFacade = frameStageFacade;
        this.interactionOnMapManager = interactionOnMapManager;
        engineConfiguration.configure();
    }

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStage(FrameStageDataModel data, InteractionDataModel interactionDataModel) {
        this.frameStageFacade.tryToRenderFrameStageView(data);
        this.interactionOnMapManager.setInteractions(interactionDataModel.getInteractions());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.frameStageFacade.startScene();
    }
}
