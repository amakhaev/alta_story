package com.alta.computator.model.participant;

import lombok.Getter;

import java.awt.*;

/**
 * Provides the participant that has coordinates for 3 dimensions
 */
public class CoordinatedParticipant extends ParticipantComputation {

    @Getter
    private final Point startMapCoordinates;
    @Getter
    private Point startGlobalCoordinates;
    @Getter
    private final int zIndex;

    @Getter
    private Point currentMapCoordinates;
    @Getter
    private Point currentGlobalCoordinates;

    /**
     * Initialize new instance of {@link CoordinatedParticipant}
     *
     * @param uuid - the UUID of participant
     */
    public CoordinatedParticipant(String uuid, Point startMapCoordinates, int zIndex, ParticipatType participatType) {
        super(uuid, participatType);
        this.startMapCoordinates = startMapCoordinates;
        this.currentMapCoordinates = new Point(startMapCoordinates.x, startMapCoordinates.y);
        this.startGlobalCoordinates = new Point();
        this.currentGlobalCoordinates = new Point();
        this.zIndex = zIndex;
    }

    /**
     * Updates the value of start global coordinates
     *
     * @param x - the X coordinate
     * @param y - the Y coordinate
     */
    public void updateStartGlobalCoordinates(int x, int y) {
        this.startGlobalCoordinates.x = x;
        this.startGlobalCoordinates.y = y;
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

    /**
     * Updates the value of current map coordinates
     *
     * @param x - the X coordinate
     * @param y - the Y coordinate
     */
    public void updateCurrentMapCoordinates(int x, int y) {
        this.currentMapCoordinates.x = x;
        this.currentMapCoordinates.y = y;
    }
}
