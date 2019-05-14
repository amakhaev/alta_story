package com.alta.computator.service.movement.directionCalculation;

import com.alta.computator.model.altitudeMap.AltitudeMap;

import java.awt.*;

/**
 * Provides the calculator of direction for participant's movement.
 */
public interface DirectionCalculator {

    /**
     * Gets the direction for future movement.
     */
    MovementDirection getDirection();

    /**
     * Gets the target point for moving.
     *
     * @param movementDirection     - the direction that used to calculate target point.
     * @param startMapCoordinates   - the start map coordinates.
     * @return the target point for moving.
     */
    Point getTargetPointForMoving(MovementDirection movementDirection, Point startMapCoordinates);

    /**
     * Indicates if object can be moved to given map coordinates
     *
     * @param altitudeMap - the {@link AltitudeMap} instance
     * @param mapCoordinates - the target map coordinates.
     * @return true if move process can be run for given direction, false otherwise
     */
    boolean isCanMoveTo(Point mapCoordinates, AltitudeMap altitudeMap);

}
