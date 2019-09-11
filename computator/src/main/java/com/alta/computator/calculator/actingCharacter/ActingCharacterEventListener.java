package com.alta.computator.calculator.actingCharacter;

import java.awt.*;

/**
 * Provides the listener for events with acting character.
 */
public interface ActingCharacterEventListener {

    /**
     * Handles the event that invoked when acting character targeted to jump point on map.
     *
     * @param targetPoint           - the point to which character was targeted.
     * @param actingCharacterUuid   - the uuid of acting character.
     */
    void onAfterMovingToJumpPoint(Point targetPoint, String actingCharacterUuid);

}