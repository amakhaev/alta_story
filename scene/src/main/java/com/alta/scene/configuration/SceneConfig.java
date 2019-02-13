package com.alta.scene.configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the config file for scee project
 */
@Getter
@Setter
public class SceneConfig {

    private ApplicationConfig app;
    private AppGameContainerConfig uiContainer;

}
