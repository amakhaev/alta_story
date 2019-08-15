package com.alta.computator.service.computator.movement.directionCalculation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

/**
 * Provides the description of route.
 */
@RequiredArgsConstructor
public class RouteMovementDescription {
    private final int x;
    private final int y;
    private Point mapCoordinate;

    @Getter
    private final MovementDirection finalDirection;

    /**
     * Gets the map coordinate of point.
     */
    public Point getMapCoordinate() {
        if (this.mapCoordinate == null) {
            this.mapCoordinate = new Point(this.x, this.y);
        }

        return this.mapCoordinate;
    }
}
