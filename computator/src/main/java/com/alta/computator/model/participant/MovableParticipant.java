package com.alta.computator.model.participant;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the movable participant of movable.
 */
public class MovableParticipant extends ParticipantComputation {

    @Getter
    private final Point startMapCoordinates;

    @Getter
    @Setter
    private Point currentMapCoordinates;

    @Getter
    @Setter
    private Point currentGlobalCoordinates;

    /**
     * Initialize new instance of {@link MovableParticipant}
     */
    public MovableParticipant(Point startMapCoordinates, String uuid) {
        super(uuid);
        this.startMapCoordinates = startMapCoordinates;
    }
}
