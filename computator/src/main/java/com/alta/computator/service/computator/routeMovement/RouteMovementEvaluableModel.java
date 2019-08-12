package com.alta.computator.service.computator.routeMovement;

import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.computator.ComputatorEvaluableModel;
import com.alta.computator.service.computator.movement.MovementWorker;
import com.alta.computator.service.computator.movement.directionCalculation.RouteMovementDirectionStrategy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Provides the evaluable model related to route movement.
 */
@Getter
@RequiredArgsConstructor
public class RouteMovementEvaluableModel implements ComputatorEvaluableModel {

    private final ActorParticipant participant;
    private final MovementWorker worker;
    private final RouteMovementDirectionStrategy movementDirectionStrategy;

    @Setter
    private int repeatingMovementTime;

    @Setter
    private boolean isComputationPause;

}
