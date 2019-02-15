package com.alta.mediator.sceneUtility;

import com.alta.scene.frameStorage.FrameTemplate;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;

@Slf4j
public class FrameImpl implements FrameTemplate {

    private TiledMap map;

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
                this.map = new TiledMap("dao/src/main/resources/data/maps/test/map.tmx");
            }

            return this.map;
        } catch (SlickException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
