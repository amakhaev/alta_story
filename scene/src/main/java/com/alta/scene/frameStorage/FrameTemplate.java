package com.alta.scene.frameStorage;

import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;

/**
 * Provides the frame that should be rendered
 */
public interface FrameTemplate {

    /**
     * Gets the start position of frame to render
     */
    Point getStartPosition();

    /**
     * Gets the tiled map related to frame to render
     */
    TiledMap getTiledMap();

}
