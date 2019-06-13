package com.alta.mediator.configuration;

import com.alta.engine.core.eventProducer.EngineEvent;
import com.alta.eventStream.EventProducer;
import com.alta.mediator.EngineEventDispatcher;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.inject.Named;

/**
 * Provides the configuration to initialize the Mediator
 */
@Singleton
public class MediatorConfiguration {

    private final EngineEventDispatcher engineEventDispatcher;
    private final EventProducer<EngineEvent> eventProducer;

    /**
     * Initialize new instance of {@link MediatorConfiguration}.
     */
    @Inject
    public MediatorConfiguration(@Named("engineEventProducer") EventProducer<EngineEvent> eventProducer,
                                 EngineEventDispatcher engineEventDispatcher) {
        this.eventProducer = eventProducer;
        this.engineEventDispatcher = engineEventDispatcher;
    }

    /**
     * Configures the mediator project.
     */
    public void configure() {
        this.bindConsumerToEngineProducer();
    }

    private void bindConsumerToEngineProducer() {
        this.eventProducer.subscribe(this.engineEventDispatcher::dispatch);
        this.eventProducer.start();
    }
}
