package com.alta.engine.entityProvision.entities;

import com.alta.scene.entities.FrameTemplate;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;
import java.util.UUID;

/**
 * Provides the base implementation of frame template
 */
@Slf4j
public class BaseFrameTemplate implements FrameTemplate, UniqueObject {

    private final UUID uuid;
    private final String pathToMap;
    private TiledMap map;

    /**
     * Initialize new instance of {@link BaseFrameTemplate}
     */
    public BaseFrameTemplate(String pathToMap) {
        this.pathToMap = pathToMap;
        this.uuid = UUID.randomUUID();
    }

    /**
     * Gets the unique identifier of object
     *
     * @return the {@link UUID} instance
     */
    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Initializes the renderable object in GL context if needed.
     *
     * @param container - the game container instance.
     */
    @Override
    public void initialize(GameContainer container) {
        log.debug("Start frame initialization");
        try {
            log.debug("Tiled map path: {}", this.pathToMap);
            this.map = new TiledMap(this.pathToMap);
        } catch (SlickException e) {
            log.error(e.getMessage());
        }
        log.debug("Frame initialization completed");
    }

    /**
     * Renders the object on given coordinates
     *
     * @param coordinates - the coordinates for render
     */
    @Override
    public void render(Point coordinates) {
        if (this.map == null) {
            log.warn("The map is null");
            return;
        }

        if (coordinates == null) {
            log.warn("Coordinates for rendering map is null");
            return;
        }

        this.map.render(coordinates.x, coordinates.y);
    }

    /**
     * Gets the tiled map related to frame to render
     */
    @Override
    public TiledMap getTiledMap() {
        return this.map;
    }
}
