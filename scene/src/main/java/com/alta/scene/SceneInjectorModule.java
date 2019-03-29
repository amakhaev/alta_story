package com.alta.scene;

import com.alta.scene.configuration.SceneConfig;
import com.alta.scene.di.AppGameContainerProvider;
import com.alta.scene.di.SceneYamlProvider;
import com.alta.scene.messageBox.MessageBoxManager;
import com.alta.scene.messageBox.MessageBoxManagerImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.AppGameContainer;

/**
 * Provides module describes DI for scene project
 */
@Slf4j
class SceneInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SceneConfig.class)
                .annotatedWith(Names.named("sceneConfig"))
                .toProvider(SceneYamlProvider.class)
                .in(Singleton.class);

        bind(AppGameContainer.class).toProvider(AppGameContainerProvider.class).in(Singleton.class);
        bind(MessageBoxManager.class).to(MessageBoxManagerImpl.class);
    }
}
