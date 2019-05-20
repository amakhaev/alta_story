package com.alta.computator.service.participantComputator.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.actor.RouteNpcParticipant;
import com.alta.computator.service.movement.MovementFactory;
import com.alta.computator.service.movement.directionCalculation.MovementDirection;
import com.alta.computator.service.movement.directionCalculation.RouteMovementDirectionStrategy;

import java.awt.*;

/**
 * Provides the computator for the NPC that has route of movement.
 */
public class RouteNpcComputator extends NpcComputator {

    private final RouteMovementDirectionStrategy movementDirectionStrategy;

    /**
     * Initialize new instance of {@link RouteNpcComputator}
     */
    RouteNpcComputator(RouteNpcParticipant routeNpcParticipant) {
        super(routeNpcParticipant, routeNpcParticipant.getMovementSpeed());
        this.movementDirectionStrategy = MovementFactory.createRouteNpcStrategy(
                routeNpcParticipant.isRouteLooped(), routeNpcParticipant.getRouteDescription()
        );
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
        if (this.movementDirectionStrategy.isRouteCompleted()) {
            if (this.npcParticipant.getCurrentDirection() != this.movementDirectionStrategy.getDirection()) {
                this.npcParticipant.setCurrentDirection(this.movementDirectionStrategy.getDirection());
            }

            this.calculateGlobalCoordinates(altitudeMap, focusPointGlobalCoordinates);
            this.repeatingMovementTime += delta;
            if (this.repeatingMovementTime > this.npcParticipant.getRepeatingMovementDurationTime()) {
                this.movementDirectionStrategy.calculateMovement(
                        this.npcParticipant.getCurrentMapCoordinates(), null, altitudeMap
                );
                this.tryToRunMovementForRoute(altitudeMap);
                this.repeatingMovementTime = 0;
            }
        } else {
            this.continueAlongRoute(altitudeMap);
        }
    }

    private void tryToRunMovementForRoute(AltitudeMap altitudeMap) {
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

    private void continueAlongRoute(AltitudeMap altitudeMap) {
        MovementDirection direction = this.movementDirectionStrategy.getDirection();
        if (direction == null) {
            return;
        }

        Point targetMapPoint = this.movementDirectionStrategy.getTargetPointForMoving();
        this.npcParticipant.setCurrentDirection(direction);

        if (this.movementDirectionStrategy.isCanMoveTo(targetMapPoint, altitudeMap)) {
            this.tryToRunMovement(targetMapPoint, altitudeMap);
        } else {
            this.movementDirectionStrategy.recalculatePartOfRoute(
                    this.npcParticipant.getCurrentMapCoordinates(),
                    altitudeMap
            );
        }
    }
}
