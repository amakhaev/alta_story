package com.alta.scene.di;

import com.alta.scene.SceneContainer;
import com.alta.scene.configuration.SceneConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import javax.inject.Named;

/**
 * Describes the effect app game container
 */
@Slf4j
public class AppGameContainerProvider implements Provider<AppGameContainer> {

    private AppGameContainer gameContainer;

    /**
     * Initialize new instance of {@link AppGameContainerProvider}
     */
    @Inject
    public AppGameContainerProvider(@Named("sceneConfig") SceneConfig config, SceneContainer container) {
        try {
            this.gameContainer = new AppGameContainer(container);
            this.gameContainer.setDisplayMode(
                    config.getUiContainer().getWidth(),
                    config.getUiContainer().getHeight(),
                    config.getUiContainer().isFullScreen()
            );
            this.gameContainer.setTargetFrameRate(config.getUiContainer().getFpsLimit());
        } catch (SlickException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Provides an instance of {@code T}.
     */
    @Override
    public AppGameContainer get() {
        return this.gameContainer;
    }
}
