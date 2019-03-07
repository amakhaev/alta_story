package com.alta.mediator;

import com.alta.mediator.di.ThreadPoolProvider;
import com.alta.mediator.domain.actor.ActorDataProvider;
import com.alta.mediator.domain.actor.ActorDataProviderImpl;
import com.alta.mediator.domain.frameStage.FrameStageDataProvider;
import com.alta.mediator.domain.frameStage.FrameStageDataProviderImpl;
import com.alta.utils.ThreadPoolExecutor;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class MediatorInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ThreadPoolExecutor.class).toProvider(ThreadPoolProvider.class).in(Singleton.class);
        bind(FrameStageDataProvider.class).to(FrameStageDataProviderImpl.class);
        bind(ActorDataProvider.class).to(ActorDataProviderImpl.class);
    }
}
