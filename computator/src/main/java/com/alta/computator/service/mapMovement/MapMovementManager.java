package com.alta.computator.service.mapMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.model.participant.map.MapParticipant;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.UUID;

/**
 * Provides the manager to make computations related to map.
 */
@Slf4j
public class MapMovementManager {

    private final FocusPointComputator focusPointComputator;
    private final MapComputator mapComputator;

    /**
     * Initialize ew instance of {@link MapMovementManager}.
     *
     * @param mapStartPosition - the start position of focus point of map.
     */
    public MapMovementManager(Point mapStartPosition) {
        FocusPointParticipant focusPointParticipant = new FocusPointParticipant(mapStartPosition, UUID.randomUUID().toString());
        focusPointParticipant.updateCurrentMapCoordinates(mapStartPosition.x, mapStartPosition.y);
        this.focusPointComputator = new FocusPointComputator(focusPointParticipant);
        log.info("Added focus point to stage with UUID: {}", focusPointParticipant.getUuid());

        this.mapComputator = new MapComputator(new MapParticipant(UUID.randomUUID().toString()));
        log.info("Added map participant to stage with UUID: {}", this.mapComputator.getMapParticipant().getUuid());
    }

    /**
     * Handles the computing of coordinates for npcMovement participants.
     *
     * @param altitudeMap - the altitude map
     */
    public void onCompute(AltitudeMap altitudeMap) {
        this.focusPointComputator.onCompute(altitudeMap);
        this.mapComputator.onCompute(
                altitudeMap, this.focusPointComputator.getFocusPointParticipant().getCurrentGlobalCoordinates()
        );
    }

    /**
     * Tries to run movement process.
     *
     * @param movementDirection - the direction of participant
     * @param altitudeMap       - the {@link AltitudeMap} instance.
     */
    public void tryToRunMovement(MovementDirection movementDirection, AltitudeMap altitudeMap) {
        this.focusPointComputator.tryToRunMovement(movementDirection, altitudeMap);
    }

    /**
     * Sets the pause on computation process.
     *
     * @param isPause - indicates when calculation should be paused.
     */
    public void setPause(boolean isPause) {
        this.focusPointComputator.setComputationPause(isPause);
    }

    /**
     * Gets the global coordinates of map participant
     *
     * @return the {@link Point} or null if not exists
     */
    public Point getMapGlobalCoordinates() {
        return this.mapComputator.getMapParticipant() != null ?
                this.mapComputator.getMapParticipant().getCurrentGlobalCoordinates() : null;
    }

    /**
     * Gets the global coordinates of focus point participant
     *
     * @return the {@link Point} or null if not exists
     */
    public Point getFocusPointGlobalCoordinates() {
        return this.focusPointComputator.getFocusPointParticipant().getCurrentGlobalCoordinates();
    }

    /**
     * Gets the map coordinates of focus point participant
     *
     * @return the {@link Point} or null if not exists
     */
    public Point getFocusPointMapCoordinates() {
        return this.focusPointComputator.getFocusPointParticipant().getCurrentMapCoordinates();
    }

    /**
     * Gets the start global coordinates of focus point.
     *
     * @return the {@link Point} or null if not exists.
     */
    public Point getFocusPointGlobalStartCoordinates() {
        return this.focusPointComputator.getConstantGlobalStartCoordination();
    }

    /**
     * Gets the last movement direction of focus point.
     *
     * @return the {@link MovementDirection} identifier.
     */
    public MovementDirection getFocusPointLastMovementDirection() {
        return this.focusPointComputator.getLastMovementDirection();
    }

    /**
     * Indicates when focus point is moving.
     *
     * @return true if focus point moving right now, false otherwise.
     */
    public boolean isFocusPointMoving() {
        return this.focusPointComputator.isMoving();
    }

    /**
     * Sets the event listener of focus point.
     *
     * @param eventListener - the {@link FocusPointEventListener} instance.
     */
    public void setFocusPointEventListener(FocusPointEventListener eventListener) {
        this.focusPointComputator.setEventListener(eventListener);
    }
}
