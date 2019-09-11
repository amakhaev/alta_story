package com.alta.computator.facade.dataReader;

import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActorParticipant;

import java.awt.*;
import java.util.List;

/**
 * Provides the facade that control read process.
 */
public interface DataReaderFacade {

    /**
     * Gets the global coordinates of map.
     *
     * @return the {@link Point} instance.
     */
    Point getMapGlobalCoordinates();

    /**
     * Gets the layer of sorted participants.
     *
     * @return the {@link List} of participant.
     */
    List<CoordinatedParticipant> getSortedParticipants();

    /**
     * Finds the actor participant by given UUID.
     *
     * @param uuid - the uuid of actor.
     * @return the {@link ActorParticipant} instance.
     */
    ActorParticipant findActorByUuid(String uuid);

    /**
     * Finds the participant to which acting character is aimed now.
     *
     * @return the {@link TargetedParticipantSummary} instance or null.
     */
    TargetedParticipantSummary findParticipantTargetedByActingCharacter();
}
