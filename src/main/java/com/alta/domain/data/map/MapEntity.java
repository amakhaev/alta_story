package com.alta.domain.data.map;

import lombok.Getter;
import org.newdawn.slick.tiled.TiledMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Provided the map entity related to tiled map.
 */
public class MapEntity {

    private final static String BARRIER_LAYER_NAME = "barriers";
    private final static String BACKGROUND_LAYER_NAME = "backgrounds";
    private final static String JUMP_UP_LEFT_LAYER_NAME = "up_left";
    private final static String JUMP_UP_MIDDLE_LAYER_NAME = "up_middle";
    private final static String JUMP_UP_RIGHT_LAYER_NAME = "up_right";
    private final static String JUMP_RIGHT_UP_LAYER_NAME = "right_up";
    private final static String JUMP_RIGHT_MIDDLE_LAYER_NAME = "right_middle";
    private final static String JUMP_RIGHT_DOWN_LAYER_NAME = "right_down";
    private final static String JUMP_DOWN_LEFT_LAYER_NAME = "down_left";
    private final static String JUMP_DOWN_MIDDLE_LAYER_NAME = "down_middle";
    private final static String JUMP_DOWN_RIGHT_LAYER_NAME = "down_right";
    private final static String JUMP_LEFT_UP_LAYER_NAME = "left_up";
    private final static String JUMP_LEFT_MIDDLE_LAYER_NAME = "left_middle";
    private final static String JUMP_LEFT_DOWN_LAYER_NAME = "left_down";

    private final static Map<String, TileType> layers = new HashMap<String, TileType>() {{
        put(BACKGROUND_LAYER_NAME, TileType.FREE);
        put(BARRIER_LAYER_NAME, TileType.BARRIER);
        put(JUMP_UP_LEFT_LAYER_NAME, TileType.JUMP_UP_LEFT);
        put(JUMP_UP_MIDDLE_LAYER_NAME, TileType.JUMP_UP_MIDDLE);
        put(JUMP_UP_RIGHT_LAYER_NAME, TileType.JUMP_UP_RIGHT);
        put(JUMP_RIGHT_UP_LAYER_NAME, TileType.JUMP_RIGHT_UP);
        put(JUMP_RIGHT_MIDDLE_LAYER_NAME, TileType.JUMP_RIGHT_MIDDLE);
        put(JUMP_RIGHT_DOWN_LAYER_NAME, TileType.JUMP_RIGHT_DOWN);
        put(JUMP_DOWN_LEFT_LAYER_NAME, TileType.JUMP_DOWN_LEFT);
        put(JUMP_DOWN_MIDDLE_LAYER_NAME, TileType.JUMP_DOWN_MIDDLE);
        put(JUMP_DOWN_RIGHT_LAYER_NAME, TileType.JUMP_DOWN_RIGHT);
        put(JUMP_LEFT_UP_LAYER_NAME, TileType.JUMP_LEFT_UP);
        put(JUMP_LEFT_MIDDLE_LAYER_NAME, TileType.JUMP_LEFT_MIDDLE);
        put(JUMP_LEFT_DOWN_LAYER_NAME, TileType.JUMP_LEFT_DOWN);
    }};

    @Getter
    private final TiledMap tiledMap;

    /**
     * Initialize new instance of {@link MapEntity}
     *
     * @param tiledMap - the tiled map.
     */
    public MapEntity(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    /**
     * Gets the altitude map.
     */
    public TileType[][] getAltitudeMap() {
        TileType[][] altitudeMap = new TileType[this.tiledMap.getWidth()][this.tiledMap.getHeight()];
        for (int x = 0; x < this.tiledMap.getWidth(); x++) {
            for (int y = 0; y < this.tiledMap.getHeight(); y++) {
                altitudeMap[x][y] = this.match(x, y);
            }
        }

        return altitudeMap;
    }

    private TileType match(int x, int y) {
        int layerIndex = 0;
        for (String key: layers.keySet()) {
            layerIndex = this.tiledMap.getLayerIndex(key);
            if (this.tiledMap.getTileId(x, y, layerIndex) != 0) {
                return layers.get(key);
            }
        }

        return TileType.FREE;
    }
}
