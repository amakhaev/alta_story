package com.alta.scene.configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the configuration for game container
 */
@Getter
@Setter
public class AppGameContainerConfig {

    private int width;
    private int height;
    private boolean fullScreen;
    private int fpsLimit;

}
