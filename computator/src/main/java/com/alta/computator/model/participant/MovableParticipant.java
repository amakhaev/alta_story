package com.alta.computator.model.participant;

import lombok.Getter;

import java.awt.*;

/**
 * Provides the movable participant of movable.
 */
public class MovableParticipant extends ParticipantComputation {

    @Getter private final Point startMapCoordinates;

    @Getter private Point currentMapCoordinates;
    @Getter private Point currentGlobalCoordinates;

    /**
     * Initialize new instance of {@link MovableParticipant}
     */
    public MovableParticipant(Point startMapCoordinates, String uuid) {
        super(uuid);
        this.startMapCoordinates = startMapCoordinates;
        this.currentMapCoordinates = new Point(0, 0);
        this.currentGlobalCoordinates = new Point(0, 0);
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
