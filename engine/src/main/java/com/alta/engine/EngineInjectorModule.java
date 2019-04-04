package com.alta.engine;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.engine.core.engineEventStream.EngineEventStream;
import com.alta.engine.view.FrameStageView;
import com.alta.engine.view.ViewFactory;
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

        bind(new TypeLiteral<EngineEventStream<ComputatorEvent>>(){})
                .annotatedWith(Names.named("computatorEventStream"))
                .toInstance(new EngineEventStream<>());


    }

}
