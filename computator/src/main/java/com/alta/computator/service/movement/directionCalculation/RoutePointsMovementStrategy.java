package com.alta.computator.service.movement.directionCalculation;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.utils.MovementRouteComputator;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Deque;
import java.util.List;

/**
 * Provides the movement strategy that find the route for moving.
 */
@Slf4j
public class RoutePointsMovementStrategy implements RouteMovementDirectionStrategy {

    private final boolean isRouteLooped;
    private final List<RouteMovementDescription> routeDescription;
    private final MovementRouteComputator movementRouteComputator;
    private Integer pointIndex;
    private Deque<Point> currentRoute;
    private Point currentStepPoint;

    /**
     * Initialize new instance of {@link RoutePointsMovementStrategy}.
     */
    public RoutePointsMovementStrategy(boolean isRouteLooped, List<RouteMovementDescription> RouteMovementDescription) {
        this.isRouteLooped = isRouteLooped;
        this.routeDescription = RouteMovementDescription;
        this.pointIndex = 0;
        this.movementRouteComputator = new MovementRouteComputator();
    }

    /**
     * Calculates the parameters for next movement.
     *
     * @param startPoint  - the start point for calculation.
     * @param direction   - the direction of movement.
     * @param altitudeMap - the altitude map instance.
     */
    @Override
    public void calculateMovement(Point startPoint, MovementDirection direction, AltitudeMap altitudeMap) {
        if (this.pointIndex == null) {
            this.pointIndex = 0;
        } else {
            if (this.pointIndex < this.routeDescription.size() - 1) {
                this.pointIndex++;
            } else {
                this.pointIndex = this.isRouteLooped ? 0 : this.pointIndex;
            }
        }

        this.calculateRoute(startPoint, this.routeDescription.get(this.pointIndex).getMapCoordinate(), altitudeMap);
    }

    /**
     * Re-Calculates the route based on current position.
     *
     * @param startPoint  - the start point of recalculation.
     * @param altitudeMap - the altitude map instance.
     */
    @Override
    public void recalculatePartOfRoute(Point startPoint, AltitudeMap altitudeMap) {
        this.calculateRoute(startPoint, this.routeDescription.get(this.pointIndex).getMapCoordinate(), altitudeMap);
    }

    /**
     * Gets the direction for future movement.
     */
    @Override
    public MovementDirection getDirection() {
        if (this.pointIndex == null) {
            return MovementDirection.DOWN;
        }

        if (this.currentRoute == null || this.currentRoute.isEmpty()) {
            return this.routeDescription.get(this.pointIndex).getFinalDirection();
        }

        return this.calculateDirection(this.currentStepPoint, this.currentRoute.peekFirst());
    }

    /**
     * Gets the target point for moving.
     *
     * @return the target point for moving.
     */
    @Override
    public Point getTargetPointForMoving() {
        this.currentStepPoint = this.currentRoute == null ? null : this.currentRoute.pollFirst();
        return this.currentStepPoint;
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
        if (mapCoordinates == null || altitudeMap == null) {
            log.debug("Null arguments. targetMapCoordinates: {}, altitudeMap: {}", mapCoordinates, altitudeMap);
            return false;
        }

        TileState tileState = altitudeMap.getTileState(mapCoordinates.x, mapCoordinates.y);
        log.debug("Try to check move availability to {}. State is {}", mapCoordinates, tileState);
        return tileState == TileState.FREE || tileState == TileState.JUMP;
    }

    /**
     * Indicates when the route of movement completed.
     *
     * @return true if route was completed, false otherwise.
     */
    @Override
    public boolean isRouteCompleted() {
        return this.currentRoute == null || this.currentRoute.isEmpty();
    }

    private void calculateRoute(Point startPoint, Point endPoint, AltitudeMap altitudeMap) {
        try {
            log.debug("Start route calculating.");
            this.currentRoute = this.movementRouteComputator.calculateRoute(startPoint, endPoint, altitudeMap);
            log.debug("Start route calculating.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private MovementDirection calculateDirection(Point from, Point to) {
        if (from == null || to == null || from.equals(to)) {
            return MovementDirection.DOWN;
        }

        if (from.x != to.x) {
            return from.x > to.x ? MovementDirection.LEFT : MovementDirection.RIGHT;
        }

        return from.y > to.y ? MovementDirection.UP : MovementDirection.DOWN;
    }
}
