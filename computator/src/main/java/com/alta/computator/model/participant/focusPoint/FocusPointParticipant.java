package com.alta.computator.model.participant.focusPoint;

import com.alta.computator.model.participant.ParticipantComputation;
import com.alta.computator.model.participant.ParticipatType;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the movable participant.
 */
public class FocusPointParticipant extends ParticipantComputation {

    @Getter
    private final Point startMapCoordinates;

    @Getter
    private Point currentMapCoordinates;
    @Getter
    private Point currentGlobalCoordinates;

    /**
     * Initialize new instance of {@link FocusPointParticipant}
     */
    public FocusPointParticipant(Point startMapCoordinates, String uuid) {
        super(uuid, ParticipatType.FOCUS_POINT);
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
