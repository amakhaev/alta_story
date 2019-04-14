package com.alta.engine;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.engine.view.FrameStageView;
import com.alta.engine.view.ViewFactory;
import com.alta.eventStream.EventProducer;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class EngineInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(FrameStageView.class, FrameStageView.class)
                .build(ViewFactory.class));

        bind(new TypeLiteral<EventProducer<ComputatorEvent>>(){})
                .annotatedWith(Names.named("computatorActionProducer"))
                .toInstance(new EventProducer<>());

        /*bind(new TypeLiteral<EventProducer<EngineEvent>>(){})
                .annotatedWith(Names.named("engineEventProducer"))
                .toInstance(new EventProducer<>());*/
    }

}
