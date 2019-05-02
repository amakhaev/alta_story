package com.alta.mediator;

import com.alta.engine.Engine;
import com.alta.engine.eventProducer.EngineEvent;
import com.alta.eventStream.EventProducer;
import com.alta.mediator.command.frameStage.FrameStageCommandFactory;
import com.alta.mediator.command.frameStage.RenderFrameStageCommand;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.mediator.di.ThreadPoolProvider;
import com.alta.mediator.domain.actor.ActorDataProvider;
import com.alta.mediator.domain.actor.ActorDataProviderImpl;
import com.alta.mediator.domain.frameStage.FrameStageDataProvider;
import com.alta.mediator.domain.frameStage.FrameStageDataProviderImpl;
import com.alta.mediator.domain.interaction.InteractionDataProvider;
import com.alta.mediator.domain.interaction.InteractionDataProviderImpl;
import com.alta.mediator.domain.interaction.InteractionPostProcessingService;
import com.alta.mediator.domain.interaction.InteractionPostProcessingServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import java.util.concurrent.ExecutorService;

public class MediatorInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExecutorService.class).toProvider(ThreadPoolProvider.class).in(Singleton.class);
        bind(FrameStageDataProvider.class).to(FrameStageDataProviderImpl.class);
        bind(ActorDataProvider.class).to(ActorDataProviderImpl.class);
        bind(InteractionDataProvider.class).to(InteractionDataProviderImpl.class);
        bind(Engine.class).in(Singleton.class);
        bind(InteractionPostProcessingService.class).to(InteractionPostProcessingServiceImpl.class).in(Singleton.class);

        bind(Long.class)
                .annotatedWith(Names.named("currentPreservationId"))
                .toInstance(1L);

        install(new FactoryModuleBuilder()
                .implement(RenderFrameStageCommand.class, RenderFrameStageCommand.class)
                .build(FrameStageCommandFactory.class));

        install(new FactoryModuleBuilder().build(PreservationCommandFactory.class));

        bind(new TypeLiteral<EventProducer<EngineEvent>>(){})
                .annotatedWith(Names.named("engineEventProducer"))
                .toInstance(new EventProducer<>());

    }
}
