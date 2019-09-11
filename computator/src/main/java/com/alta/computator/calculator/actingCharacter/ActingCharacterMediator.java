package com.alta.computator.calculator.actingCharacter;

import java.awt.*;

/**
 * Provides the mediator to control acting character.
 */
public interface ActingCharacterMediator {

    /**
     * Sets the listener of acting character changes.
     *
     * @param eventListener - the {@link ActingCharacterEventListener} instance.
     */
    void setEventListener(ActingCharacterEventListener eventListener);

    /**
     * Finds the coordinates of target participant to which acting character is aimed
     *
     * @return the coordinates of target participant.
     */
    Point getMapCoordinatesOfTargetParticipant();
}
