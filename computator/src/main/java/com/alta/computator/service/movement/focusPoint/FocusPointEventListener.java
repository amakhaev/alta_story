package com.alta.computator.service.movement.focusPoint;

import java.awt.*;

/**
 * Provides the listener of events for focus point.
 */
public interface FocusPointEventListener {

    /**
     * Handles the event that invoked when focus point targeted to jump point on map.
     *
     * @param targetPoint - the point to which focus was targeted.
     */
    void onBeforeMovingToJumpTile(Point targetPoint);

}
