package com.alta.computator.calculator.npc;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.core.computator.ComputatorEvaluableModel;

import java.awt.*;

/**
 * Provides the functionality for movement of NPC.
 */
public interface NpcMovementService<T extends ComputatorEvaluableModel> {

    /**
     * Adds the participant into container to be used for future computations.
     *
     * @param npcParticipant - the npcMovement participant to be added.
     */
    void addParticipant(NpcParticipant npcParticipant);

    /**
     * Adds the evaluable model into container to be used for future computations.
     *
     * @param evaluableModel - the evaluable model that stored date required for computations..
     */
    void addEvaluableModel(T evaluableModel);

    /**
     * Removes the evaluable model from container.
     *
     * @param uuid - the uuid of evaluable model.
     */
    void removeEvaluableModel(String uuid);

    /**
     * Handles the computing of coordinates for npc participants.
     *
     * @param altitudeMap                   - the altitude map
     * @param focusPointGlobalCoordinates   - the global coordinates of focus point
     * @param delta                         - the time between last and previous one calls
     */
    void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta);

    /**
     * Handles the computing of coordinates for npc participants immediately.
     *
     * @param altitudeMap                   - the altitude map
     * @param focusPointGlobalCoordinates   - the global coordinates of focus point
     * @param delta                         - the time between last and previous one calls
     */
    void onComputeImmediately(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta);

    /**
     * Indicates when movement service contains evaluable model with given UUID.
     *
     * @param uuid - the UUID of participant ot be found.
     * @return true if movement service contains participant, false otherwise.
     */
    boolean hasEvaluableModel(String uuid);

    /**
     * Gets the evaluable model by given UUID.
     *
     * @param uuid - the UUID of evaluable model to be found.
     * @return the {@link T} instance.
     */
    T getEvaluableModel(String uuid);

    /**
     * Finds the NPC that has given map coordinates.
     *
     * @param mapCoordinates - the map coordinates for searching.
     * @return the {@link TargetedParticipantSummary} instance of null if not found.
     */
    TargetedParticipantSummary findNpcByMapCoordinates(Point mapCoordinates);

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
}
