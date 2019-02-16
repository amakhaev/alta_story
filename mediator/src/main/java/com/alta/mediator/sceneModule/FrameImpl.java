package com.alta.mediator.sceneModule;

import com.alta.scene.frameStorage.FrameTemplate;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;

@Slf4j
public class FrameImpl implements FrameTemplate {

    private final String pathToMap;
    private TiledMap map;

    /**
     * Initialize new instance of {@link FrameImpl}
     */
    public FrameImpl(String pathToMap) {
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
        try {
            if (this.map == null) {
                this.map = new TiledMap(this.pathToMap);
            }

            return this.map;
        } catch (SlickException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
