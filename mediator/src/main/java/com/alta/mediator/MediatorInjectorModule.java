package com.alta.mediator;

import com.alta.mediator.di.SceneProvider;
import com.alta.mediator.di.ThreadPoolProvider;
import com.alta.scene.Scene;
import com.alta.utils.ThreadPoolExecutor;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class MediatorInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Scene.class).toProvider(SceneProvider.class).in(Singleton.class);
        bind(ThreadPoolExecutor.class).toProvider(ThreadPoolProvider.class).in(Singleton.class);
    }

}
