package com.alta.computator.model.participant.actor;

import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.service.movement.strategy.MovementDirection;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the participant that describes the simple NPC
 */
public class SimpleNpcParticipant extends ActorParticipant {

    @Getter
    private final int repeatingMovementDurationTime;

    /**
     * Initialize new instance of {@link SimpleNpcParticipant}
     */
    public SimpleNpcParticipant(String uuid, Point startMapCoordinates, int zIndex, int repeatingMovementDurationTime) {
        super(uuid, startMapCoordinates, zIndex, ParticipatType.SIMPLE_NPC);
        this.repeatingMovementDurationTime = repeatingMovementDurationTime;
        this.setCurrentDirection(MovementDirection.DOWN);
    }
}
