package com.alta.computator.service.movement.strategy;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the strategy that provides computation of participants that tried to avoid obstructions
 */
@Slf4j
public class AvoidObstructionStrategy extends BaseMovementStrategy {

    /**
     * Initialize new instance of {@link AvoidObstructionStrategy}
     */
    AvoidObstructionStrategy() {}

    /**
     * Indicates if object can be moved to given map coordinates
     *
     * @param targetMapCoordinates - the target map coordinates.
     * @param altitudeMap    - the {@link AltitudeMap} instance
     * @return true if move process can be run for given direction, false otherwise
     */
    @Override
    public boolean isCanMoveTo(Point targetMapCoordinates, AltitudeMap altitudeMap) {
        TileState tileState = altitudeMap.getTileState(targetMapCoordinates.x, targetMapCoordinates.y);
        log.debug("Try to check move availability to {}. State is {}", targetMapCoordinates, tileState);
        return tileState == TileState.FREE;
    }
}
