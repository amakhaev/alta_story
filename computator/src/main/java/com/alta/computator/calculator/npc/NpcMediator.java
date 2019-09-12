package com.alta.computator.calculator.npc;

import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActorParticipant;

import java.awt.*;
import java.util.function.Function;

/**
 * Provides the mediator to control NPC.
 */
public interface NpcMediator {

    /**
     * Gets the participant by given UUID.
     *
     * @param uuid - the UUID of participant to be found.
     * @return the {@link ActorParticipant} instance.
     */
    ActorParticipant getParticipant(String uuid);

    /**
     * Finds the NPC that has given map coordinates.
     *
     * @param mapCoordinates - the map coordinates for searching.
     * @return the {@link TargetedParticipantSummary} instance of null if not found.
     */
    TargetedParticipantSummary findNpcTargetByMapCoordinates(Point mapCoordinates);

    /**
     * Sets the pause computation process of NPC.
     *
     * @param isPause - indicates when calculation should be paused.
     */
    void setPause(boolean isPause);

    /**
     * Sets the pause on participantComputator process for specific NPC.
     *
     * @param isPause   - indicates when calculation should be paused.
     * @param uuid      - the uuid of NPC to be paused
     */
    void setPause(boolean isPause, String uuid);

    /**
     * Tries to run movement for NPC participant.
     *
     * @param npcTargetUuid     - the NPC uuid.
     * @param x                 - the X coordinate to be moved.
     * @param y                 - the Y coordinate to be moved.
     * @param movementSpeed     - the speed of movement.
     * @param finalDirection    - the final direction of participant after finishing the movement.
     * @param completeCallback  - the callback to be invoked when movement completed.
     */
    void tryToRunNpcMovement(String npcTargetUuid,
                             int x,
                             int y,
                             int movementSpeed,
                             MovementDirection finalDirection,
                             Function<String, Void> completeCallback
    );
}
