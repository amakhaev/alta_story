package com.alta.computator.service.participantComputator.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import com.alta.computator.service.computator.movement.MovementFactory;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirectionStrategy;

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
     * Updates the movement of npcMovementProcessor includes the direction.
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

    /**
     * Handles the completing of movement.
     */
    protected void onAfterMovementCompleted() {
        this.npcParticipant.setMoving(false);
    }

    private void tryToRunMovement(AltitudeMap altitudeMap) {
        if (this.isComputationPause) {
            return;
        }

        this.movementDirectionStrategy.calculateMovement(this.npcParticipant.getCurrentMapCoordinates(), null, altitudeMap);
        MovementDirection direction = this.movementDirectionStrategy.getDirection();
        if (direction == null) {
            return;
        }

        Point targetMapPoint = this.movementDirectionStrategy.getTargetPointForMoving();

        this.npcParticipant.setCurrentDirection(direction);

        if (this.movementDirectionStrategy.isCanMoveTo(targetMapPoint, altitudeMap)) {
            this.tryToRunMovement(targetMapPoint, altitudeMap);
            this.npcParticipant.setMoving(true);
        } else {
            this.repeatingMovementTime = 0;
        }
    }
}
