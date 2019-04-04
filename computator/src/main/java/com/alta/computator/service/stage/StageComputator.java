package com.alta.computator.service.stage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;

import java.awt.*;
import java.util.List;

/**
 * Provides the computator that makes some calculations related to frame stage.
 */
public interface StageComputator {

    /**
     * Sets the altitude map.
     *
     * @param altitudeMap - the map to be set.
     */
    void setAltitudeMap(AltitudeMap altitudeMap);

    /**
     * Gets the global coordinates of focus point participant
     *
     * @return the {@link Point} or null if not exists
     */
    Point getMapGlobalCoordinates();

    /**
     * Gets the list of participants in correct order for render. Order based on zIndex
     *
     * @return the {@link java.util.List} of participant.
     */
    List<CoordinatedParticipant> getSortedParticipants();

    /**
     * Gets the actor participant by given UUID
     *
     * @param uuid - the UUID of participant
     * @return the {@link SimpleNpcParticipant} instance of null if key not present
     */
    ActorParticipant getActorParticipant(String uuid);

    /**
     * Handles the next tick in the stage
     *
     * @param delta - the time between last and previous one calls
     */
    void onTick(int delta);
}
