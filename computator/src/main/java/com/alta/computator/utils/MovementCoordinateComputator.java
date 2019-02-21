package com.alta.computator.utils;

import lombok.experimental.UtilityClass;

/**
 * Provides the computator of movement coordinates. It should convert tiled coordinates to global.
 */
@UtilityClass
public class MovementCoordinateComputator {

    /**
     * Calculates the global coordinates of start position.
     *
     * @param objectSize - the size of object
     * @param mapStartPosition - the relative position on map
     * @return global coordinate of object
     */
    public int calculateGlobalStartCoordinateOfObject(int objectSize, int mapStartPosition) {
        return objectSize * mapStartPosition;
    }

    /**
     * Calculates the start coordinate of object on center of screen
     *
     * @param objectSize - the size of object
     * @param screenSize - the screen size
     * @return
     */
    public int calculateGlobalStartCoordinateOnCenterOfScreen(int objectSize, int screenSize) {
        return screenSize / 2 - objectSize / 2;
    }

}
