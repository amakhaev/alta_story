package com.alta.mediator;

import com.alta.mediator.di.ThreadPoolProvider;
import com.alta.mediator.domain.frameStage.FrameStageService;
import com.alta.mediator.domain.frameStage.FrameStageServiceImpl;
import com.alta.utils.ThreadPoolExecutor;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class MediatorInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ThreadPoolExecutor.class).toProvider(ThreadPoolProvider.class).in(Singleton.class);
        bind(FrameStageService.class).to(FrameStageServiceImpl.class);
    }
}
