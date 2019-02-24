package com.alta.computator.service.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.FocusPointParticipant;
import com.alta.computator.model.participant.MapParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.UUID;

/**
 * Provides the computations that help with movement on stage e.g. frame, actors
 */
@Slf4j
public class StageComputator {

    @Setter @Getter private AltitudeMap altitudeMap;

    private FocusPointComputator focusPointComputator;
    private MapComputator mapComputator;

    /**
     * Adds the participant that presented focus point
     *
     * @param mapStartPosition - the start position of participant
     */
    public void addFocusPointParticipant(Point mapStartPosition) {
        if (mapStartPosition == null) {
            throw new RuntimeException("The coordinates of focus point must be set.");
        }
        FocusPointParticipant focusPointParticipant = new FocusPointParticipant(mapStartPosition, UUID.randomUUID().toString());
        focusPointParticipant.updateCurrentMapCoordinates(mapStartPosition.x, mapStartPosition.y);
        this.focusPointComputator = new FocusPointComputator(focusPointParticipant);
        log.debug("Added focus point to stage with UUID: {}", focusPointParticipant.getUuid());

        this.mapComputator = new MapComputator(new MapParticipant(UUID.randomUUID().toString()));
        log.debug("Added map participant to stage with UUID: {}", this.mapComputator.getMapParticipant().getUuid());
    }

    /**
     * Handles the next tick in the stage
     */
    public void onTick() {
        if (!this.isAllDataInitialized()) {
            log.warn("One or more computator data wasn't initialized. No any action will be performed");
            return;
        }

        this.focusPointComputator.onCompute(this.altitudeMap);
        this.mapComputator.onCompute(
                this.altitudeMap,
                this.focusPointComputator.getFocusPointParticipant().getCurrentGlobalCoordinates()
        );
    }

    /**
     * Tries to run movement process. If process successfully ran then coordinates will update after calling onTick method
     *
     * @param movementDirection - the direction of movement
     */
    public void tryToRunMovement(MovementDirection movementDirection) {
        this.focusPointComputator.tryToRunMovement(movementDirection, this.altitudeMap);
    }

    /**
     * Gets the global coordinates of focus point participant
     *
     * @return the {@link Point} or null if not exists
     */
    public Point getFocusPointGlobalCoordinates() {
        return this.focusPointComputator != null &&
                this.focusPointComputator.getConstantGlobalStartCoordination() != null ?
                this.focusPointComputator.getConstantGlobalStartCoordination() : null;
    }

    /**
     * Gets the global coordinates of focus point participant
     *
     * @return the {@link Point} or null if not exists
     */
    public Point getMapGlobalCoordinates() {
        return this.mapComputator != null && this.mapComputator.getMapParticipant() != null ?
                this.mapComputator.getMapParticipant().getCurrentGlobalCoordinates() : null;
    }

    private boolean isAllDataInitialized() {
        if (this.altitudeMap == null) {
            log.warn("Altitude map is not set.");
            return false;
        } else if (this.focusPointComputator == null || this.focusPointComputator.getFocusPointParticipant() == null) {
            log.warn("The focus point participant is not set.");
            return false;
        }

        return true;
    }
}
