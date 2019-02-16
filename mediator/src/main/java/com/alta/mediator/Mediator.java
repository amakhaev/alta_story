package com.alta.mediator;

import com.alta.dao.DaoInjectorModule;
import com.alta.mediator.sceneModule.SceneProxy;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Provides the mediator that handles events from modules
 */
public class Mediator {

    private final SceneProxy sceneProxy;

    /**
     * Initialize new instance of {@link Mediator}
     */
    public Mediator() {
        Injector injector = Guice.createInjector(new MediatorInjectorModule(), new DaoInjectorModule());
        this.sceneProxy = injector.getInstance(SceneProxy.class);
    }

    /**
     * Starts the scene
     */
    public void loadSavedSceneAndStart() {
        this.sceneProxy.loadSceneFromPreservation();
        this.sceneProxy.sceneStart();
    }
}
