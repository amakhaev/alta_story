package com.alta.computator.model.event;

import lombok.Getter;

import java.awt.*;

/**
 * Provides the event that describes jump of acting charater
 */
public class ActingCharacterJumpEvent extends ComputatorEvent {

    @Getter
    private final Point mapCoordinates;

    /**
     * Initialize new instance of ComputatorEvent
     */
    public ActingCharacterJumpEvent(ComputatorEventType computatorEventType, Point mapCoordinates) {
        super(computatorEventType);
        this.mapCoordinates = mapCoordinates;
    }
}
