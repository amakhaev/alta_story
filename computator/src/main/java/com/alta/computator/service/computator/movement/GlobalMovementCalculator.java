package com.alta.computator.service.computator.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;

import java.awt.*;

/**
 * Provides the interface for calculates the participantComputator of participant
 */
public interface GlobalMovementCalculator {

    /**
     * Determinate the speed as number by given string representation.
     *
     * @param representation - the string representation of speed.
     * @return the speed as number.
     */
    static int determinateSpeed(String representation) {
        switch (representation) {
            case "SLOW":
                return GlobalMovementCalculatorImpl.SLOW_MOVE_SPEED;
            case "NORMAL":
                return GlobalMovementCalculatorImpl.NORMAL_MOVE_SPEED;
            case "FAST":
                return GlobalMovementCalculatorImpl.FAST_MOVE_SPEED;
            default:
                throw new RuntimeException("Unknown type of movement speed: " + representation);
        }
    }

    /**
     * Indicates if participantComputator already running or not
     *
     * @return true if participantComputator in progress, false otherwise
     */
    boolean isCurrentlyRunning();

    /**
     * Tries to run moving if it possible to given global coordinate
     *
     * @param altitudeMap - the altitude map instance
     * @param mapCoordinatesFrom - the map coordinates of starting participantComputator
     * @param mapCoordinatesTarget - the map coordinates of target point
     */
    void tryToRunMoveProcess(AltitudeMap altitudeMap, Point mapCoordinatesFrom, Point mapCoordinatesTarget);

    /**
     * Updates the coordinates of participantComputator
     */
    void onUpdate();

    /**
     * Gets the map coordinates of target position of participantComputator
     */
    Point getMapTargetCoordinates();

    /**
     * Gets the global coordinates of current position of participantComputator
     */
    Point getGlobalCurrentCoordinates();

    /**
     * Clears the results of calculation for the last participantComputator
     */
    void clearLastMovement();
}
