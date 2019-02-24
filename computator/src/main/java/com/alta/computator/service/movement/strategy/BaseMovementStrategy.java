package com.alta.computator.service.movement.strategy;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the base implementation of movement strategy
 */
@Slf4j
public abstract class BaseMovementStrategy implements MovementStrategy {

    private static final int DEFAULT_MOVE_SPEED = 5;
    private static final int DIRECTION_FORWARD = 1;
    private static final int DIRECTION_BACKWARD = -1;
    private static final int DIRECTION_NEUTRAL = 0;
    
    private final int moveSpeed;

    private Point globalCurrentCoordinates;
    private Point mapTargetCoordinates;
    private Point globalTargetCoordinates;
    private int directionByX;
    private int directionByY;
    private boolean isRunning;

    /**
     * Initialize new instance of {@link BaseMovementStrategy}
     */
    BaseMovementStrategy() {
        this.moveSpeed = DEFAULT_MOVE_SPEED;
        this.isRunning = false;
    }

    /**
     * Indicates if movement already running or not
     *
     * @return true if movement in progress, false otherwise
     */
    @Override
    public boolean isCurrentlyRunning() {
        return this.isRunning;
    }

    /**
     * Tries to run moving if it possible to given global coordinate
     *
     * @param altitudeMap - the altitude map instance
     * @param globalCoordinatesFrom - the global coordinates of starting movement
     * @param mapCoordinatesTarget - the map coordinates of target point
     * @param globalCoordinatesTarget - the global coordinates of target point
     */
    public synchronized void tryToRunMoveProcess(AltitudeMap altitudeMap,
                                                 Point globalCoordinatesFrom,
                                                 Point mapCoordinatesTarget,
                                                 Point globalCoordinatesTarget) {
        if (!this.isCanMoveTo(mapCoordinatesTarget, altitudeMap)) {
            log.warn("Can't run move process to map coordinates: {}", mapCoordinatesTarget);
            return;
        } else if (this.isCurrentlyRunning()) {
            log.warn("Can't run move process because it already ran");
            return;
        }

        this.isRunning = true;
        this.globalCurrentCoordinates = globalCoordinatesFrom;
        this.globalTargetCoordinates = globalCoordinatesTarget;
        this.mapTargetCoordinates = mapCoordinatesTarget;

        this.directionByX = this.calculateDirectionValue(this.globalCurrentCoordinates.x, this.globalTargetCoordinates.x);
        this.directionByY = this.calculateDirectionValue(this.globalCurrentCoordinates.y, this.globalTargetCoordinates.y);
        log.debug("Started animation from {} to {}", globalCoordinatesFrom, globalTargetCoordinates);
    }

    /**
     * Updates the coordinates of movement
     */
    public synchronized void onUpdate() {
        if (!this.isCurrentlyRunning()) {
            return;
        }

        this.globalCurrentCoordinates.x = this.calculateCoordinate(
                this.directionByX, this.globalCurrentCoordinates.x, this.globalTargetCoordinates.x
        );

        this.globalCurrentCoordinates.y = this.calculateCoordinate(
                this.directionByY, this.globalCurrentCoordinates.y, this.globalTargetCoordinates.y
        );

        this.isRunning = this.isMovementRunning();
    }

    /**
     * Gets the map coordinates of target position of movement
     */
    @Override
    public Point getMapTargetCoordinates() {
        return this.mapTargetCoordinates;
    }

    /**
     * Gets the global coordinates of current position of movement
     */
    @Override
    public Point getGlobalCurrentCoordinates() {
        return this.globalCurrentCoordinates;
    }

    /**
     * Clears the results of calculation for the last movement
     */
    @Override
    public void clearLastMovement() {
        this.globalCurrentCoordinates = null;
        this.mapTargetCoordinates = null;
        this.globalTargetCoordinates = null;
        this.directionByX = DIRECTION_NEUTRAL;
        this.directionByY = DIRECTION_NEUTRAL;
        this.isRunning = false;
    }

    private int calculateCoordinate(int direction, int current, int target) {
        if (direction == DIRECTION_FORWARD) {
            if (current <= target - this.moveSpeed) {
                current += this.moveSpeed;
            }
            else {
                current = target;
            }
        }
        else {
            if (current >= target + this.moveSpeed) {
                current -= this.moveSpeed;
            }
            else {
                current = target;
            }
        }

        return current;
    }

    private boolean isMovementRunning() {
        return this.globalCurrentCoordinates.x != this.globalTargetCoordinates.x ||
                this.globalCurrentCoordinates.y != this.globalTargetCoordinates.y;
    }

    private int calculateDirectionValue(int globalFromCoordinate, int globalTargetCoordinate) {
        return Integer.compare(globalTargetCoordinate, globalFromCoordinate);
    }
}
