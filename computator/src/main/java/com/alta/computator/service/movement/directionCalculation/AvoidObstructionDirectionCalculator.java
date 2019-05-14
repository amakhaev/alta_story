package com.alta.computator.service.movement.directionCalculation;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the direction calculator that will avoid any obstruction.
 */
@Slf4j
public class AvoidObstructionDirectionCalculator implements DirectionCalculator {
    /**
     * Gets the direction for future movement.
     */
    @Override
    public MovementDirection getDirection() {
        return MovementDirection.randomDirection();
    }

    /**
     * Gets the target point for moving.
     *
     * @param movementDirection   - the direction that used to calculate target point.
     * @param startMapCoordinates - the start map coordinates.
     * @return the target point for moving.
     */
    @Override
    public Point getTargetPointForMoving(@NonNull MovementDirection movementDirection, @NonNull Point startMapCoordinates) {
        Point targetMapPoint = new Point(startMapCoordinates);
        switch (movementDirection) {
            case UP:
                targetMapPoint.y--;
                break;
            case DOWN:
                targetMapPoint.y++;
                break;
            case LEFT:
                targetMapPoint.x--;
                break;
            case RIGHT:
                targetMapPoint.x++;
                break;
        }

        return targetMapPoint;
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
}
