package com.alta.engine;

import com.alta.engine.configuration.EngineConfiguration;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import com.alta.engine.utils.dataBuilder.FrameStageData;
import com.google.inject.Inject;

/**
 * Provides the engine that contains logic related to scene and calculation
 */
public class Engine {

    private final FrameStagePresenter frameStagePresenter;
    private final MessageBoxPresenter messageBoxPresenter;

    /**
     * Initialize new instance of {@link Engine}
     */
    @Inject
    public Engine(FrameStagePresenter frameStagePresenter,
                  MessageBoxPresenter messageBoxPresenter,
                  EngineConfiguration engineConfiguration) {
        engineConfiguration.configure();
        this.frameStagePresenter = frameStagePresenter;
        this.messageBoxPresenter = messageBoxPresenter;
    }

    /**
     * Loads scene state from characterPreservation
     */
    public void tryToRenderFrameStage(FrameStageData data) {
        this.frameStagePresenter.tryToRenderFrameStageView(data);
        this.messageBoxPresenter.forceHideMessageBox();
        this.messageBoxPresenter.showTitle(data.getMapDisplayName());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.frameStagePresenter.startScene();
    }
}
