package com.alta.mediator.di;

import com.alta.scene.Scene;
import com.google.inject.Provider;

public class SceneProvider implements Provider<Scene> {

    private Scene scene;

    /**
     * Initialize new instance of {@link SceneProvider}
     */
    public SceneProvider() {
        this.scene = new Scene();
    }

    /**
     * Provides an instance of {@code T}.
     */
    @Override
    public Scene get() {
        return this.scene;
    }
}
