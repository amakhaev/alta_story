package com.alta.scene.entities;

import org.newdawn.slick.tiled.TiledMap;

/**
 * Provides the frame that should be rendered
 */
public interface FrameTemplate {

    /**
     * Gets the tiled map related to frame to render
     */
    TiledMap getTiledMap();

    /**
     * Initializes the frame
     */
    void initializeFrame();

}
