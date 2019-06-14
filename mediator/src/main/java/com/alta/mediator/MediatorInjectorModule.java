package com.alta.mediator;

import com.alta.engine.Engine;
import com.alta.engine.data.EngineRepository;
import com.alta.interaction.dataSource.InteractionRepository;
import com.alta.mediator.command.frameStage.FrameStageCommandFactory;
import com.alta.mediator.command.interaction.InteractionCommandFactory;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.mediator.dataSource.EngineRepositoryImpl;
import com.alta.mediator.dataSource.InteractionRepositoryImpl;
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
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import java.util.concurrent.ExecutorService;

public class MediatorInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Long.class).annotatedWith(Names.named("currentPreservationId")).toInstance(1L);

        install(new FactoryModuleBuilder().build(FrameStageCommandFactory.class));
        install(new FactoryModuleBuilder().build(PreservationCommandFactory.class));
        install(new FactoryModuleBuilder().build(InteractionCommandFactory.class));

        bind(ExecutorService.class).toProvider(ThreadPoolProvider.class).in(Singleton.class);
        bind(FrameStageDataProvider.class).to(FrameStageDataProviderImpl.class);
        bind(ActorDataProvider.class).to(ActorDataProviderImpl.class);
        bind(InteractionDataProvider.class).to(InteractionDataProviderImpl.class);
        bind(Engine.class).in(Singleton.class);
        bind(InteractionPostProcessingService.class).to(InteractionPostProcessingServiceImpl.class).in(Singleton.class);

        bind(InteractionRepository.class).to(InteractionRepositoryImpl.class).in(Singleton.class);
        bind(EngineRepository.class).to(EngineRepositoryImpl.class).in(Singleton.class);
    }
}
