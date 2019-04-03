package com.alta.engine;

import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import com.alta.engine.presenter.actionDispatcher.InputActionDispatcher;
import com.alta.engine.presenter.sceneProxy.sceneInput.ActionEventListener;
import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;
import com.alta.engine.utils.dataBuilder.FrameStageData;
import com.alta.engine.presenter.sceneProxy.sceneInput.KeyActionProducer;
import com.alta.engine.utils.listener.engineEvent.EngineListener;
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
                  KeyActionProducer keyActionProducer,
                  InputActionDispatcher actionDispatcher) {
        this.frameStagePresenter = frameStagePresenter;
        this.messageBoxPresenter = messageBoxPresenter;

        keyActionProducer.setListener(new ActionEventListener() {
            @Override
            public void onPerformAction(SceneAction action) {
                actionDispatcher.dispatchConstantlyAction(action);
            }

            @Override
            public void onActionReleased(SceneAction action) {
                actionDispatcher.dispatchReleaseAction(action);
            }
        });
    }

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStage(FrameStageData data) {
        this.frameStagePresenter.tryToRenderFrameStageView(data);
        this.messageBoxPresenter.showTitle(data.getMapDisplayName());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.frameStagePresenter.startScene();
    }

    /**
     * Sets the listener of events from engine.
     *
     * @param engineListener - the listener of engine events.
     */
    public void setEngineListener(EngineListener engineListener) {
        this.frameStagePresenter.setEngineListener(engineListener);
    }
}
