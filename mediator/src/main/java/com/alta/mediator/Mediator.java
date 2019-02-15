package com.alta.mediator;

import com.alta.scene.Scene;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Provides the mediator that handles events from modules
 */
public class Mediator {

    private Scene scene;

    public Mediator() {
        Injector injector = Guice.createInjector(new MediatorInjetorModule());
        this.scene = injector.getInstance(Scene.class);
    }

    /**
     * Starts the scene
     */
    public void sceneStart() {
        this.scene.start();
    }
}
