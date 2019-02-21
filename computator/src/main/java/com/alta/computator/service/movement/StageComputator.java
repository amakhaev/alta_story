package com.alta.computator.service.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.MovableParticipant;
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
    private AltitudeMap altitudeMap;

    private MovableParticipant focusPointParticipant;

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
        log.debug("Added focus point to stage with UUID: {}", this.focusPointParticipant.getUuid());
    }

    /**
     * Handles the next tick in the stage
     */
    public void onTick() {
        if (this.altitudeMap == null) {
            log.warn("Altitude map is not set. No any action will be performed");
            return;
        } else if (this.focusPointParticipant == null) {
            log.warn("The focus point participant is not set. No any action will be performed");
            return;
        }

        log.info("Tick called");
    }
}
