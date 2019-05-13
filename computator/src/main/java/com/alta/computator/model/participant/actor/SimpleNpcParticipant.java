package com.alta.computator.model.participant.actor;

import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.movement.strategy.MovementStrategyFactory;
import lombok.Getter;
import lombok.NonNull;

import java.awt.*;

/**
 * Provides the participant that describes the simple NPC
 */
public class SimpleNpcParticipant extends ActorParticipant {

    @Getter
    private final int repeatingMovementDurationTime;

    @Getter
    private final MovementStrategyFactory.Strategy movementStrategy;

    /**
     * Initialize new instance of {@link SimpleNpcParticipant}
     */
    public SimpleNpcParticipant(@NonNull String uuid,
                                @NonNull Point startMapCoordinates,
                                int zIndex,
                                int repeatingMovementDurationTime,
                                @NonNull MovementStrategyFactory.Strategy movementStrategy,
                                @NonNull MovementDirection initialDirection) {
        super(uuid, startMapCoordinates, zIndex, ParticipatType.SIMPLE_NPC);
        this.repeatingMovementDurationTime = repeatingMovementDurationTime;
        this.movementStrategy = movementStrategy;
        this.setCurrentDirection(initialDirection);
    }
}
