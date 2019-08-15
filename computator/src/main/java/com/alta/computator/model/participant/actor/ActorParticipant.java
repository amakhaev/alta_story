package com.alta.computator.model.participant.actor;

import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the participant that describes the actor
 */
public abstract class ActorParticipant extends CoordinatedParticipant {

    @Getter
    @Setter
    private MovementDirection currentDirection;

    @Getter
    @Setter
    private boolean isMoving;

    @Getter
    private final int repeatingMovementDurationTime;

    /**
     * Initialize new instance of {@link CoordinatedParticipant}
     */
    ActorParticipant(String uuid, Point startMapCoordinates, int zIndex, ParticipatType participatType, int repeatingMovementDurationTime) {
        super(uuid, startMapCoordinates, zIndex, participatType);
        this.repeatingMovementDurationTime = repeatingMovementDurationTime;
    }
}
