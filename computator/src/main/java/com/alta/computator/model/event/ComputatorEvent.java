package com.alta.computator.model.event;

import lombok.Getter;

/**
 * Provides the event that related to computator
 */
public abstract class ComputatorEvent {

    @Getter
    private final ComputatorEventType computatorEventType;

    /**
     * Initialize new instance of ComputatorEvent
     */
    protected ComputatorEvent(ComputatorEventType computatorEventType) {
        this.computatorEventType = computatorEventType;
    }
}
