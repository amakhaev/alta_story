package com.alta.computator.service.participantComputator.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import com.alta.computator.service.movement.MovementFactory;
import com.alta.computator.service.movement.directionCalculation.MovementDirection;
import com.alta.computator.service.movement.directionCalculation.MovementDirectionStrategy;

import java.awt.*;

/**
 * Provides the calculations for list of simple NPC
 */
class SimpleNpcComputator extends NpcComputator {

    private final MovementDirectionStrategy movementDirectionStrategy;

    /**
     * Initialize new instance of {@link SimpleNpcComputator}
     */
    SimpleNpcComputator(SimpleNpcParticipant simpleNpcParticipant) {
        super(simpleNpcParticipant);
        this.movementDirectionStrategy = MovementFactory.createSimpleNpcStrategy(simpleNpcParticipant.getMovementType());
    }

    /**
     * Updates the movement of npc includes the direction.
     *
     * @param altitudeMap                 - the {@link AltitudeMap} instance.
     * @param focusPointGlobalCoordinates - the global coordinates of focus point.
     * @param delta                       - the time between last and previous one calls.
     */
    @Override
    protected void updateMovement(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        this.calculateGlobalCoordinates(altitudeMap, focusPointGlobalCoordinates);
        this.repeatingMovementTime += delta;
        if (this.repeatingMovementTime > this.npcParticipant.getRepeatingMovementDurationTime()) {
            this.tryToRunMovement(altitudeMap);
            this.repeatingMovementTime = 0;
        }
    }

    private void tryToRunMovement(AltitudeMap altitudeMap) {
        this.movementDirectionStrategy.calculateMovement(this.npcParticipant.getCurrentMapCoordinates(), null, altitudeMap);
        MovementDirection direction = this.movementDirectionStrategy.getDirection();
        if (direction == null) {
            return;
        }

        Point targetMapPoint = this.movementDirectionStrategy.getTargetPointForMoving();

        this.npcParticipant.setCurrentDirection(direction);

        if (this.movementDirectionStrategy.isCanMoveTo(targetMapPoint, altitudeMap)) {
            this.tryToRunMovement(targetMapPoint, altitudeMap);
        } else {
            this.repeatingMovementTime = 0;
        }
    }
}
