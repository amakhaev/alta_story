package com.alta.scene.containerEvents;

/**
 * Provides the event that indicates about update
 */
@FunctionalInterface
public interface UpdateEvent {

    /**
     * Handles the update event
     */
    void onUpdate();

}
