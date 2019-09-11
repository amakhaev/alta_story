package com.alta.computator.calculator.focusPoint;

import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;

/**
 * Provides the mediator to control focus point.
 */
public interface FocusPointMediator {

    /**
     * Tries to run movement action. If process successfully ran then coordinates will update after updating.
     *
     * @param movementDirection - the direction of participant.
     */
    void tryToRunMovement(MovementDirection movementDirection);

    /**
     * Sets the event listener of focus point.
     *
     * @param eventListener - the {@link FocusPointEventListener}.
     */
    void setEventListener(FocusPointEventListener eventListener);

    /**
     * Indicates when move process is running.
     *
     * @return true if moving between two map coordinates is running now, false otherwise.
     */
    boolean isMoving();

    /**
     * Sets the pause of focus point.
     *
     * @param isPause - indicates when calculation should be paused.
     */
    void setPause(boolean isPause);
}
