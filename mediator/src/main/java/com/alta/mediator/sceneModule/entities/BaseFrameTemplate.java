package com.alta.mediator.sceneModule.entities;

import com.alta.scene.entities.FrameTemplate;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;

@Slf4j
public class BaseFrameTemplate implements FrameTemplate {

    private final String pathToMap;
    private TiledMap map;

    /**
     * Initialize new instance of {@link BaseFrameTemplate}
     */
    public BaseFrameTemplate(String pathToMap) {
        this.pathToMap = pathToMap;
    }

    /**
     * Gets the start position of frame to render
     */
    @Override
    public Point getStartPosition() {
        return new Point(0, 0);
    }

    /**
     * Gets the tiled map related to frame to render
     */
    @Override
    public TiledMap getTiledMap() {
        return this.map;
    }

    /**
     * Initializes the frame
     */
    @Override
    public void initializeFrame() {
        log.debug("Start frame initialization");
        try {
            log.debug("Tiled map path: {}", this.pathToMap);
            this.map = new TiledMap(this.pathToMap);
        } catch (SlickException e) {
            log.error(e.getMessage());
        }
        log.debug("Frame initialization completed");
    }
}
