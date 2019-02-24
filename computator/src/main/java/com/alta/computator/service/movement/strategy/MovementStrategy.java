package com.alta.computator.service.movement.strategy;

import com.alta.computator.model.altitudeMap.AltitudeMap;

import java.awt.*;

/**
 * Provides the interface for calculates the movement of participant
 */
public interface MovementStrategy {

    /**
     * Indicates if movement already running or not
     *
     * @return true if movement in progress, false otherwise
     */
    boolean isCurrentlyRunning();

    /**
     * Indicates if object can be moved to given map coordinates
     *
     * @param altitudeMap - the {@link AltitudeMap} instance
     * @param mapCoordinates - the target map coordinates.
     * @return true if move process can be run for given direction, false otherwise
     */
    boolean isCanMoveTo(Point mapCoordinates, AltitudeMap altitudeMap);

    /**
     * Tries to run moving if it possible to given global coordinate
     *
     * @param altitudeMap - the altitude map instance
     * @param globalCoordinatesFrom - the global coordinates of starting movement
     * @param mapCoordinatesTarget - the map coordinates of target point
     * @param globalCoordinatesTarget - the global coordinates of target point
     */
    void tryToRunMoveProcess(AltitudeMap altitudeMap,
                             Point globalCoordinatesFrom,
                             Point mapCoordinatesTarget,
                             Point globalCoordinatesTarget);

    /**
     * Updates the coordinates of movement
     */
    void onUpdate();

    /**
     * Gets the map coordinates of target position of movement
     */
    Point getMapTargetCoordinates();

    /**
     * Gets the global coordinates of current position of movement
     */
    Point getGlobalCurrentCoordinates();

    /**
     * Clears the results of calculation for the last movement
     */
    void clearLastMovement();
}
