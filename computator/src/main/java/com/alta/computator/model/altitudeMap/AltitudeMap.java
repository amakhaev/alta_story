package com.alta.computator.model.altitudeMap;

import com.alta.computator.computationExceptions.AltitudeMapException;
import lombok.Getter;
import org.newdawn.slick.tiled.TiledMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the altitude map
 */
public class AltitudeMap {

    private static final String BARRIER_LAYER_NAME = "barriers";
    private static final String FREE_LAYER_NAME = "backgrounds";

    private final static Map<String, TileState> AVAILABLE_LAYERS = new HashMap<String, TileState>() {{
        put(FREE_LAYER_NAME, TileState.FREE);
        put(BARRIER_LAYER_NAME, TileState.BLOCKED);
    }};

    private TileState[][] initialTileStates;

    @Getter private final int screenWidth;
    @Getter private final int screenHeight;
    @Getter private final int tileWidth;
    @Getter private final int tileHeight;

    /**
     * Initialize new instance of {@link AltitudeMap}
     */
    public AltitudeMap(TiledMap tiledMap, int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.createTileStates(tiledMap);
        this.tileWidth = tiledMap.getTileWidth();
        this.tileHeight = tiledMap.getTileHeight();
    }

    /**
     * Gets the state of tile that indicates how to interpreter selected tile
     *
     * @param x - the x coordinates on map
     * @param y - the y coordinates on map
     * @return the {@link TileState} instance
     */
    public TileState getTileState(int x, int y) {
        if (this.initialTileStates.length < x || this.initialTileStates[0].length < y) {
            return null;
        }

        return this.initialTileStates[x][y];
    }

    private void createTileStates(TiledMap tiledMap) {
        if (tiledMap == null) {
            throw new AltitudeMapException("Can't create tiled tile states array because no given tiled map");
        }

        this.initialTileStates = new TileState[tiledMap.getWidth()][tiledMap.getHeight()];
        for (int x = 0; x < tiledMap.getWidth(); x++) {
            for (int y = 0; y < tiledMap.getHeight(); y++) {
                this.initialTileStates[x][y] = this.getTileState(x, y, tiledMap);
            }
        }
    }

    private TileState getTileState(int x, int y, TiledMap tiledMap) {
        int layerIndex = 0;
        for (String key: AVAILABLE_LAYERS.keySet()) {
            layerIndex = tiledMap.getLayerIndex(key);
            if (tiledMap.getTileId(x, y, layerIndex) != 0) {
                return AVAILABLE_LAYERS.get(key);
            }
        }

        return TileState.FREE;
    }
}
