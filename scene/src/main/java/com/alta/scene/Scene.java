package com.alta.scene;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * Provides the base scene object
 */
@Slf4j
public class Scene {

    private AppGameContainer gameContainer;

    /**
     * Initialize new instance of {@link Scene}
     */
    public Scene() {
        Injector injector = Guice.createInjector(new SceneInjectorModule());
        this.gameContainer = injector.getInstance(AppGameContainer.class);
    }

    /**
     * Starts the scene
     */
    public void start() {
        try {
            this.gameContainer.start();
        } catch (SlickException e) {
            log.error(e.getMessage());
        }
    }

}
