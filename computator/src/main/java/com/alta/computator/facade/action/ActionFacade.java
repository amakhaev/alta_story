package com.alta.computator.facade.action;

import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.eventStream.EventProducer;

/**
 * Provides the list of high level method to make actions in the system.
 */
public interface ActionFacade {

    /**
     * Sets the listener of acting character changes.
     *
     * @param eventProducer - the {@link EventProducer} instance.
     */
    void setEventListener(EventProducer<ComputatorEvent> eventProducer);

    /**
     * Tries to run movement of acting character to given direction.
     *
     * @param movementDirection - the movement direction to be used in calculation.
     */
    void tryToRunActingCharacterMovement(MovementDirection movementDirection);

    /**
     * Sets the pause on calculations process.
     *
     * @param isPause - indicates when calculation should be paused.
     */
    void setPauseForAll(boolean isPause);

    /**
     * Sets the pause of computation process for character.
     *
     * @param isPause   - indicates when calculation should be paused.
     * @param uuid      - the uuid of NPC to be paused
     */
    void setNpcPause(boolean isPause, String uuid);

    /**
     * Tries to run movement for NPC participant.
     *
     * @param npcTargetUuid - the NPC uuid.
     * @param x             - the X coordinate to be moved.
     * @param y             - the Y coordinate to be moved.
     * @param movementSpeed     - the speed of movement.
     * @param finalDirection    - the final direction of participant after finishing the movement.
     */
    void tryToRunNpcMovement(String npcTargetUuid, int x, int y, int movementSpeed, MovementDirection finalDirection);
}
