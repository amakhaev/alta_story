package com.alta.engine.configuration;

import com.alta.engine.actionDispatcher.InputActionDispatcher;
import com.alta.engine.presenter.sceneProxy.sceneInput.ActionEventListener;
import com.alta.engine.presenter.sceneProxy.sceneInput.KeyActionProducer;
import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

/**
 * Provides the configuration class that makes initial configuration for engine.
 */
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class EngineConfiguration {

    private final KeyActionProducer keyActionProducer;
    private final InputActionDispatcher actionDispatcher;

    /**
     * Configures the engine.
     */
    public final void configure() {
        this.bindKeyActionConsumer();
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
}
