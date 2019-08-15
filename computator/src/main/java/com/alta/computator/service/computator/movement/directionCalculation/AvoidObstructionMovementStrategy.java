package com.alta.computator.service.computator.movement.directionCalculation;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the direction calculator that will avoid any obstruction.
 */
@Slf4j
public class AvoidObstructionMovementStrategy implements MovementDirectionStrategy {

    private MovementDirection currentDirection;
    private Point currentTargetPoint;

    /**
     * Calculates the parameters for next movement.
     *
     * @param startPoint        - the start point for calculation.
     * @param defaultDirection  - the direction of movement.
     * @param altitudeMap       - the altitude map instance.
     */
    @Override
    public void calculateMovement(Point startPoint, MovementDirection defaultDirection, AltitudeMap altitudeMap) {
        this.currentDirection = defaultDirection == null ? MovementDirection.randomDirection() : defaultDirection;

        this.currentTargetPoint = new Point(startPoint);
        switch (this.currentDirection) {
            case UP:
                this.currentTargetPoint.y--;
                break;
            case DOWN:
                this.currentTargetPoint.y++;
                break;
            case LEFT:
                this.currentTargetPoint.x--;
                break;
            case RIGHT:
                this.currentTargetPoint.x++;
                break;
        }
    }

    /**
     * Gets the direction for future movement.
     */
    @Override
    public MovementDirection getDirection() {
        return this.currentDirection;
    }

    /**
     * Gets the target point for moving.
     *
     * @return the target point for moving.
     */
    @Override
    public Point getTargetPointForMoving() {
        return this.currentTargetPoint;
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
        return true;
    }
}
