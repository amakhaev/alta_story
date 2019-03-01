package com.alta.scene.entities;

import com.alta.scene.core.RenderableEntity;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;

/**
 * Provides the frame that should be rendered
 */
public interface FrameTemplate extends RenderableEntity<Point> {

    /**
     * Gets the tiled map related to frame to render
     */
    TiledMap getTiledMap();

}
