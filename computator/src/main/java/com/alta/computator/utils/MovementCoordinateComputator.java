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
     * @return global coordinate of object
     */
    public int calculateGlobalStartCoordinateOnCenterOfScreen(int objectSize, int screenSize) {
        return screenSize / 2 - objectSize / 2;
    }

    /**
     * Calculates global coordinates of object based on global coordinates of another object. Suggested that another
     * object should be in center of screen.
     *
     * @param screenSize -the screen size
     * @param objectSize - the object size
     * @param currentObjectMapCoordinates - the map coordinates of current object
     * @param anotherObjectGlobalCoordinate - the global coordinate of another object
     * @return global coordinate of object
     */
    public int calculateGlobalCoordinatesDependsOnAnotherObject(int screenSize,
                                                                int objectSize,
                                                                int currentObjectMapCoordinates,
                                                                int anotherObjectGlobalCoordinate) {
        int value = calculateGlobalStartCoordinateOnCenterOfScreen(objectSize, screenSize) - anotherObjectGlobalCoordinate;
        value += MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                objectSize,
                currentObjectMapCoordinates
        );

        return value;
    }

    /**
     * Calculates the global coordinate of starting map
     *
     * @param screenSize -the screen size
     * @param objectSize - the object size
     * @param objectGlobalCoordinates - the global coordinate of object
     * @return global coordinate of map
     */
    public int calculateGlobalCoordinateOfMap(int screenSize, int objectSize, int objectGlobalCoordinates) {
        return calculateGlobalStartCoordinateOnCenterOfScreen(objectSize, screenSize) - objectGlobalCoordinates;
    }
}
