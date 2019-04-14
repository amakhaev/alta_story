package com.alta.engine.configuration;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.engine.actionDispatcher.InputActionDispatcher;
import com.alta.engine.facade.FrameStageListener;
import com.alta.engine.presenter.sceneProxy.sceneInput.ActionEventListener;
import com.alta.engine.presenter.sceneProxy.sceneInput.KeyActionProducer;
import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;
import com.alta.eventStream.EventProducer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.inject.Named;

/**
 * Provides the configuration class that makes initial configuration for engine.
 */
@Singleton
public final class EngineConfiguration {

    private final KeyActionProducer keyActionProducer;
    private final InputActionDispatcher actionDispatcher;
    private final EventProducer<ComputatorEvent> computatorActionProducer;
    private final FrameStageListener frameStageListener;

    /**
     * Initialize new instance of {@link EngineConfiguration}.
     *
     * @param computatorActionProducer  - the producer of computator events.
     * @param frameStageListener        - the listener of frame stage events.
     * @param keyActionProducer         - the producer of key pressing.
     * @param actionDispatcher          - the dispatcher of key pressing.
     */
    @Inject
    public EngineConfiguration(@Named("computatorActionProducer") EventProducer<ComputatorEvent> computatorActionProducer,
                               FrameStageListener frameStageListener,
                               KeyActionProducer keyActionProducer,
                               InputActionDispatcher actionDispatcher) {
        this.keyActionProducer = keyActionProducer;
        this.actionDispatcher = actionDispatcher;
        this.computatorActionProducer = computatorActionProducer;
        this.frameStageListener = frameStageListener;
    }

    /**
     * Configures the engine.
     */
    public final void configure() {
        this.bindKeyActionConsumer();
        this.bindComputatorConsumer();
    }

    private void bindKeyActionConsumer() {
        this.keyActionProducer.setListener(new ActionEventListener() {
            @Override
            public void onPerformAction(SceneAction action) {
                actionDispatcher.dispatchConstantlyAction(action);
            }

            @Override
            public void onPerformActionReleased(SceneAction action) {
                actionDispatcher.dispatchReleaseAction(action);
            }
        });
    }

    private void bindComputatorConsumer() {
        this.computatorActionProducer.subscribe(this.frameStageListener::handleComputatorEvent);
        this.computatorActionProducer.start();
    }
}
