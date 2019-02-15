package com.alta.mediator;

import com.alta.mediator.di.SceneProvider;
import com.alta.scene.Scene;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class MediatorInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Scene.class)
                .toProvider(SceneProvider.class)
                .in(Singleton.class);
    }

}
