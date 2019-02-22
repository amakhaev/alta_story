package com.alta.computator.model.participant;

import lombok.Getter;

import java.awt.*;

/**
 * Provides the participant that depends on coordinates another participant
 */
public class MapParticipant extends ParticipantComputation {

    @Getter
    private Point currentGlobalCoordinates;

    /**
     * Initialize new instance of {@link MapParticipant}
     *
     * @param uuid - the UUID of participant
     */
    public MapParticipant(String uuid) {
        super(uuid);
        this.currentGlobalCoordinates = new Point(0, 0);
    }

    /**
     * Updates the value of current global coordinates
     *
     * @param x - the X coordinate
     * @param y - the Y coordinate
     */
    public void updateCurrentGlobalCoordinates(int x, int y) {
        this.currentGlobalCoordinates.x = x;
        this.currentGlobalCoordinates.y = y;
    }
}
