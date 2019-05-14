package com.alta.computator.service.movement.directionCalculation;

import com.alta.computator.model.altitudeMap.AltitudeMap;

import java.awt.*;

/**
 * Provides the direction calculator that focused on one point.
 */
public class StandSpotDirectionCalculator implements DirectionCalculator {
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
     * @param movementDirection   - the direction that used to calculate target point.
     * @param startMapCoordinates - the start map coordinates.
     * @return the target point for moving.
     */
    @Override
    public Point getTargetPointForMoving(MovementDirection movementDirection, Point startMapCoordinates) {
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
}
