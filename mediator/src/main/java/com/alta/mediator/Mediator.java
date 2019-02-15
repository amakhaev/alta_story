package com.alta.mediator;

import com.alta.mediator.sceneUtility.FrameImpl;
import com.alta.mediator.sceneUtility.FrameStageImpl;
import com.alta.mediator.sceneUtility.SceneInputListener;
import com.alta.scene.Scene;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Provides the mediator that handles events from modules
 */
public class Mediator {

    private Scene scene;

    /**
     * Initialize new instance of {@link Mediator}
     */
    public Mediator() {
        Injector injector = Guice.createInjector(new MediatorInjectorModule());
        this.scene = injector.getInstance(Scene.class);
        this.scene.setInputListener(injector.getInstance(SceneInputListener.class));
        this.scene.renderStage(new FrameStageImpl(new FrameImpl()));
    }

    /**
     * Starts the scene
     */
    public void sceneStart() {
        this.scene.start();
    }
}
