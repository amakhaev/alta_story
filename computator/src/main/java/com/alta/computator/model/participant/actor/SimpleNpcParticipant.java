package com.alta.computator.model.participant.actor;

import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.core.computator.movement.MovementType;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import lombok.NonNull;

import java.awt.*;

/**
 * Provides the participant that describes the simple NPC
 */
public class SimpleNpcParticipant extends NpcParticipant {

    /**
     * Initialize new instance of {@link SimpleNpcParticipant}
     */
    public SimpleNpcParticipant(@NonNull String uuid,
                                @NonNull Point startMapCoordinates,
                                int zIndex,
                                int repeatingMovementDurationTime,
                                @NonNull MovementType movementType,
                                @NonNull MovementDirection initialDirection) {
        super(
                uuid,
                startMapCoordinates,
                zIndex,
                repeatingMovementDurationTime,
                movementType,
                initialDirection,
                ParticipatType.SIMPLE_NPC
        );
    }
}
