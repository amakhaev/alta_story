package com.alta.mediator.di;

import com.alta.mediator.sceneModule.SceneInputListener;
import com.alta.scene.Scene;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SceneProvider implements Provider<Scene> {


    private Scene scene;

    /**
     * Initialize new instance of {@link SceneProvider}
     */
    @Inject
    public SceneProvider(SceneInputListener sceneInputListener) {
        this.scene = new Scene();
        this.scene.setInputListener(sceneInputListener);
    }

    /**
     * Provides an instance of {@code T}.
     */
    @Override
    public Scene get() {
        return this.scene;
    }
}
