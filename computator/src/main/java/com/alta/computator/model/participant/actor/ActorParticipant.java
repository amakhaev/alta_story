package com.alta.computator.model.participant.actor;

import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.service.movement.strategy.MovementDirection;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the actor participant
 */
public class ActorParticipant extends CoordinatedParticipant {

    @Getter
    @Setter
    private MovementDirection currentDirection;

    @Getter
    @Setter
    private boolean isMoving;

    /**
     * Initialize new instance of {@link CoordinatedParticipant}
     *
     * @param uuid                - the UUID of participant
     */
    public ActorParticipant(String uuid, Point startMapCoordinates, int zIndex) {
        super(uuid, startMapCoordinates, zIndex, ParticipatType.ACTING_CHARACTER);
    }
}
