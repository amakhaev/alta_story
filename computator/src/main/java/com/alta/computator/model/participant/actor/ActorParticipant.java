package com.alta.computator.model.participant.actor;

import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.ParticipatType;

import java.awt.*;

/**
 * Provides the actor participant
 */
public class ActorParticipant extends CoordinatedParticipant {

    /**
     * Initialize new instance of {@link CoordinatedParticipant}
     *
     * @param uuid                - the UUID of participant
     */
    public ActorParticipant(String uuid, Point startMapCoordinates, int zIndex) {
        super(uuid, startMapCoordinates, zIndex, ParticipatType.ACTING_CHARACTER);
    }
}
