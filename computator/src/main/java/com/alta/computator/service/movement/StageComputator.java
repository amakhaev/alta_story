package com.alta.computator.service.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.MovableParticipant;
import com.alta.computator.model.participant.RelativeParticipant;
import com.alta.computator.utils.MovementCoordinateComputator;
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

    @Setter
    @Getter
    private AltitudeMap altitudeMap;

    @Getter
    private MovableParticipant focusPointParticipant;

    @Getter
    private RelativeParticipant mapParticipant;

    /**
     * Adds the participant that presented focus point
     *
     * @param mapStartPosition - the start position of participant
     */
    public void addFocusPointParticipant(Point mapStartPosition) {
        if (mapStartPosition == null) {
            throw new RuntimeException("The coordinates of focus point must be set.");
        }
        this.focusPointParticipant = new MovableParticipant(mapStartPosition, UUID.randomUUID().toString());
        this.focusPointParticipant.updateCurrentMapCoordinates(mapStartPosition.x, mapStartPosition.y);
        log.debug("Added focus point to stage with UUID: {}", this.focusPointParticipant.getUuid());
        this.mapParticipant = new RelativeParticipant(UUID.randomUUID().toString());
        log.debug("Added map participant to stage with UUID: {}", this.mapParticipant.getUuid());
    }

    /**
     * Handles the next tick in the stage
     */
    public void onTick() {
        if (!this.isAllDataInitialized()) {
            log.warn("One or more computator data wasn't initialized. No any action will be performed");
            return;
        }

        this.focusPointParticipant.setCurrentGlobalCoordinates(
                new Point(
                        MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(this.altitudeMap.getTileWidth(), this.altitudeMap.getScreenWidth()),
                        MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(this.altitudeMap.getTileHeight(), this.altitudeMap.getScreenHeight())
                )
        );

        int x = MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(this.altitudeMap.getTileWidth(), this.focusPointParticipant.getCurrentMapCoordinates().x);
        int y = MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(this.altitudeMap.getTileHeight(), this.focusPointParticipant.getCurrentMapCoordinates().y);
        this.mapParticipant.setCurrentGlobalCoordinates(
                new Point(
                        (this.altitudeMap.getScreenWidth() / 2 - this.altitudeMap.getTileWidth() / 2) - x,
                        (this.altitudeMap.getScreenHeight() / 2 - this.altitudeMap.getTileHeight() / 2) - y
                )
        );
    }

    private boolean isAllDataInitialized() {
        if (this.altitudeMap == null) {
            log.warn("Altitude map is not set.");
            return false;
        } else if (this.focusPointParticipant == null) {
            log.warn("The focus point participant is not set.");
            return false;
        }

        return true;
    }
}
