package com.alta.computator.model.altitudeMap;

import com.alta.computator.computationExceptions.AltitudeMapException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.tiled.TiledMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the altitude map
 */
@Slf4j
public class AltitudeMap {

    private static final String BARRIER_LAYER_NAME = "barriers";
    private static final String BACKGROUND_LAYER_NAME = "backgrounds";

    private final static Map<String, TileState> AVAILABLE_LAYERS = new HashMap<String, TileState>() {{
        put(BACKGROUND_LAYER_NAME, TileState.FREE);
        put(BARRIER_LAYER_NAME, TileState.BARRIER);
    }};

    private TileState[][] currentTileStates;
    private TileState[][] jumpTileStates;

    @Getter
    private final int screenWidth;

    @Getter
    private final int screenHeight;

    @Getter
    private final int tileWidth;

    @Getter
    private final int tileHeight;

    /**
     * The count of tiles that can be shown on <code>screenWidth</code>
     */
    @Getter
    private final int availableCountOfTileOnXAxis;

    /**
     * The count of tiles that can be shown on <code>screenHeight</code>
     */
    @Getter
    private final int availableCountOfTileOnYAxis;

    /**
     * The count of tiles that available on tiled map for X axis.
     */
    @Getter
    private final int totalCountOfTileOnXAxis;

    /**
     * The count of tiles that available on tiled map for Y axis.
     */
    @Getter
    private final int totalCountOfTileOnYAxis;

    /**
     * Initialize new instance of {@link AltitudeMap}
     */
    public AltitudeMap(TiledMap tiledMap, int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.createTileStates(tiledMap);
        this.tileWidth = tiledMap.getTileWidth();
        this.tileHeight = tiledMap.getTileHeight();
        this.availableCountOfTileOnXAxis = (int) Math.ceil((double) this.screenWidth / (double) this.tileWidth);
        this.availableCountOfTileOnYAxis = (int) Math.ceil((double) this.screenHeight / (double) this.tileHeight);
        this.totalCountOfTileOnXAxis = tiledMap.getWidth();
        this.totalCountOfTileOnYAxis = tiledMap.getHeight();
    }

    /**
     * Gets the state of tile that indicates how to interpreter selected tile
     *
     * @param x - the x coordinates on map
     * @param y - the y coordinates on map
     * @return the {@link TileState} instance
     */
    public TileState getTileState(int x, int y) {
        if (x < 0 || y < 0 || this.currentTileStates.length - 1 < x || this.currentTileStates[x].length - 1 < y) {
            return TileState.BARRIER;
        }

        return this.currentTileStates[x][y];
    }

    /**
     * Sets the tile state on specific position
     *
     * @param x - the coordinate of X axis
     * @param y - the coordinate of Y axis
     * @param tileState - the new state of tile
     */
    public void setTileState(int x, int y, TileState tileState) {
        if (this.currentTileStates.length < x || this.currentTileStates[0].length < y) {
            log.error(
                    "Invalid x: {} and y: {} arguments. Length of array is [{},{}]",
                    x,
                    y,
                    this.currentTileStates.length,
                    this.currentTileStates[0].length
            );
            return;
        }

        if (tileState == null) {
            log.error("Null tile state");
            return;
        }

        this.currentTileStates[x][y] = tileState;
        if (tileState == TileState.JUMP) {
            this.jumpTileStates[x][y] = tileState;
        }
    }

    /**
     * Indicates when given map coordinates references to jump tile state
     *
     * @param x - the x coordinates on map
     * @param y - the y coordinates on map
     * @return true if point has jump tile state, false otherwise
     */
    public boolean isJumpTileState(int x, int y) {
        return this.jumpTileStates[x][y] == TileState.JUMP;
    }

    private void createTileStates(TiledMap tiledMap) {
        if (tiledMap == null) {
            throw new AltitudeMapException("Can't create tiled map states array because no given tiled map");
        }

        this.currentTileStates = new TileState[tiledMap.getWidth()][tiledMap.getHeight()];
        this.jumpTileStates = new TileState[tiledMap.getWidth()][tiledMap.getHeight()];
        for (int x = 0; x < tiledMap.getWidth(); x++) {
            for (int y = 0; y < tiledMap.getHeight(); y++) {
                this.currentTileStates[x][y] = this.getTileState(x, y, tiledMap);
            }
        }
    }

    private TileState getTileState(int x, int y, TiledMap tiledMap) {
        int layerIndex = 0;
        for (String key: AVAILABLE_LAYERS.keySet()) {
            layerIndex = tiledMap.getLayerIndex(key);
            try {
                if (layerIndex != -1 && tiledMap.getTileId(x, y, layerIndex) != 0) {
                    return AVAILABLE_LAYERS.get(key);
                }
            } catch (Exception e) {
                log.error("Can't determinate tile state: {}:{}. {}", x, y, e.getMessage());
            }
        }

        return TileState.FREE;
    }
}
