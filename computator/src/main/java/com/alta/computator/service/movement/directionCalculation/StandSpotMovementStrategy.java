package com.alta.computator.service.movement.directionCalculation;

import com.alta.computator.model.altitudeMap.AltitudeMap;

import java.awt.*;

/**
 * Provides the direction calculator that focused on one point.
 */
public class StandSpotMovementStrategy implements MovementDirectionStrategy {

    /**
     * Calculates the parameters for next movement.
     *
     * @param startPoint  - the start point for calculation.
     * @param direction   - the direction of movement.
     * @param altitudeMap - the altitude map instance.
     */
    @Override
    public void calculateMovement(Point startPoint, MovementDirection direction, AltitudeMap altitudeMap) {

    }

    /**
     * Gets the direction for future movement.
     */
    @Override
    public MovementDirection getDirection() {
        return null;
    }

    /**
     * Gets the target point for moving.
     *
     * @return the target point for moving.
     */
    @Override
    public Point getTargetPointForMoving() {
        return null;
    }

    /**
     * Indicates if object can be moved to given map coordinates
     *
     * @param mapCoordinates - the target map coordinates.
     * @param altitudeMap    - the {@link AltitudeMap} instance
     * @return true if move process can be run for given direction, false otherwise
     */
    @Override
    public boolean isCanMoveTo(Point mapCoordinates, AltitudeMap altitudeMap) {
        return false;
    }

    /**
     * Indicates when the route of movement completed.
     *
     * @return true if route was completed, false otherwise.
     */
    @Override
    public boolean isRouteCompleted() {
        return true;
    }
}
