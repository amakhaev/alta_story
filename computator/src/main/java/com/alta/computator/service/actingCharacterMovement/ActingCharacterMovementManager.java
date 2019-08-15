package com.alta.computator.service.actingCharacterMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;

import java.awt.*;

/**
 * Provides the manager to make computations related to acting character.
 */
public class ActingCharacterMovementManager {
    
    private final ActingCharacterComputator actingCharacterComputator;

    /**
     * Initialize new instance of {@link ActingCharacterMovementManager}.
     *
     * @param participant - the {@link ActingCharacterParticipant} instance.
     */
    public ActingCharacterMovementManager(ActingCharacterParticipant participant) {
        this.actingCharacterComputator = new ActingCharacterComputator(participant);
    }

    /**
     * Handles the computing of map map objects
     *
     * @param altitudeMap - the altitude map instance
     * @param focusPointMapCoordinates - the map coordinates of focus point
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     * @param direction - the direction of last moving.
     * @param isMoving - indicates when move process is running
     */
    public void onCompute(AltitudeMap altitudeMap,
                          Point focusPointMapCoordinates,
                          Point focusPointGlobalCoordinates,
                          MovementDirection direction,
                          boolean isMoving) {
        this.actingCharacterComputator.onCompute(
                altitudeMap, focusPointMapCoordinates, focusPointGlobalCoordinates, direction, isMoving
        );
    }

    /**
     * Gets the acting character participant.
     *
     * @return the {@link ActingCharacterParticipant} instance.
     */
    public ActingCharacterParticipant getActingCharacterParticipant() {
        return this.actingCharacterComputator.getActingCharacterParticipant();
    }

    /**
     * Finds the coordinates of target participant to which acting character is aimed
     *
     * @return the coordinates of target participant.
     */
    public Point getMapCoordinatesOfTargetParticipant() {
        return this.actingCharacterComputator.getMapCoordinatesOfTargetParticipant();
    }

    /**
     * Sets the event listener for acting character.
     *
     * @param listener - the event listener instance.
     */
    public void setEventListener(ActingCharacterEventListener listener) {
        this.actingCharacterComputator.setEventListener(listener);
    }
}
