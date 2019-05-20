package com.alta.computator.model.participant.actor;

import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.service.movement.MovementType;
import com.alta.computator.service.movement.directionCalculation.MovementDirection;
import lombok.Getter;
import lombok.NonNull;

import java.awt.*;

/**
 * Provides the participant that describes the NPC.
 */
public abstract class NpcParticipant extends ActorParticipant {

    @Getter
    private final int repeatingMovementDurationTime;

    @Getter
    private final MovementType movementType;

    /**
     * Initialize new instance of {@link NpcParticipant}
     */
    public NpcParticipant(@NonNull String uuid,
                          @NonNull Point startMapCoordinates,
                          int zIndex,
                          int repeatingMovementDurationTime,
                          @NonNull MovementType movementType,
                          @NonNull MovementDirection initialDirection,
                          @NonNull ParticipatType npcParticipantType) {
        super(uuid, startMapCoordinates, zIndex, npcParticipantType);
        this.repeatingMovementDurationTime = repeatingMovementDurationTime;
        this.movementType = movementType;
        this.setCurrentDirection(initialDirection);
    }

}
