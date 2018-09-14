package com.alta.domain.map;

import java.awt.*;

/**
 * Provides the calculator to get coordinates of objects related to map.
 */
public class MapCoordinateCalculatorUtils {

    /**
     * Calculates the global coordinates of tile.
     *
     * @param tileWidth - the tile width.
     * @param tileX - the X coordinate of tile.
     * @return the global coordinate point.
     */
    public static int calculateOffsetTileGlobalX(int tileWidth, int tileX) {
        return tileX * tileWidth;
    }

    /**
     * Calculates the global coordinates of tile.
     *
     * @param tileHeight - the tile height.
     * @param tileY - the Y coordinate of tile.
     * @return the global coordinate point.
     */
    public static int calculateOffsetTileGlobalY(int tileHeight, int tileY) {
        return tileY * tileHeight;
    }

    public static int calculateGlobalCenterPositionOfTileX(int tileWidth, int screenWidth) {
        return screenWidth / 2 - tileWidth / 2;
    }

    public static int calculateGlobalCenterPositionOfTileY(int tileHeight, int screenHeight) {
        return screenHeight / 2 - tileHeight / 2;
    }

    /**
     * Calculates the start position of map by centered tile.
     *
     * @param tileWidth - the tile width.
     * @param screenWidth - the screen width.
     * @return the global coordinates of map.
     */
    public static int calculateCenterByOffsetX(int globalOffsetX, int tileWidth, int screenWidth) {
        int centerPositionForTile = calculateGlobalCenterPositionOfTileX(tileWidth, screenWidth);
        return centerPositionForTile - globalOffsetX;
    }

    /**
     * Calculates the start position of map by centered tile.
     *
     * @param tileHeight - the tile width.
     * @param screenHeight - the screen width.
     * @return the global coordinates of map.
     */
    public static int calculateCenterByOffsetY(int globalOffsetY, int tileHeight, int screenHeight) {
        int centerPositionForTile = calculateGlobalCenterPositionOfTileY(tileHeight, screenHeight);
        return centerPositionForTile - globalOffsetY;
    }
}
