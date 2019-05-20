package com.alta.computator.service.movement.directionCalculation;

import com.alta.computator.model.altitudeMap.AltitudeMap;

import java.awt.*;

/**
 * Provides the interface for route movement strategy.
 */
public interface RouteMovementDirectionStrategy extends MovementDirectionStrategy {

    /**
     * Indicates if route was completed.
     *
     * @return true if route completed, false otherwise.
     */
    boolean isRouteCompleted();

    /**
     * Re-Calculates the part of route.
     *
     * @param startPoint    - the start point of recalculation.
     * @param altitudeMap   - the altitude map instance.
     *
     */
    void recalculatePartOfRoute(Point startPoint, AltitudeMap altitudeMap);
}
