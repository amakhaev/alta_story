package com.alta.computator.service.movement.directionCalculation;

import com.alta.computator.model.altitudeMap.AltitudeMap;

import java.awt.*;

/**
 * Provides the calculator of direction for participant's movement.
 */
public interface MovementDirectionStrategy {

    /**
     * Calculates the parameters for next movement.
     *
     * @param startPoint    - the start point for calculation.
     * @param direction     - the direction of movement.
     * @param altitudeMap   - the altitude map instance.
     */
    void calculateMovement(Point startPoint, MovementDirection direction, AltitudeMap altitudeMap);

    /**
     * Gets the direction for future movement.
     */
    MovementDirection getDirection();

    /**
     * Gets the target point for moving.
     * @return the target point for moving.
     */
    Point getTargetPointForMoving();

    /**
     * Indicates if object can be moved to given map coordinates
     *
     * @param altitudeMap - the {@link AltitudeMap} instance
     * @param mapCoordinates - the target map coordinates.
     * @return true if move process can be run for given direction, false otherwise
     */
    boolean isCanMoveTo(Point mapCoordinates, AltitudeMap altitudeMap);

    /**
     * Indicates when the route of movement completed.
     *
     * @return true if route was completed, false otherwise.
     */
    boolean isRouteCompleted();

}
