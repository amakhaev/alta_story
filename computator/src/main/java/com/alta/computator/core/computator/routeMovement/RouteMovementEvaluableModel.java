package com.alta.computator.core.computator.routeMovement;

import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.core.computator.ComputatorEvaluableModel;
import com.alta.computator.core.computator.movement.GlobalMovementCalculator;
import com.alta.computator.core.computator.movement.directionCalculation.RouteMovementDirectionStrategy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.function.Function;

/**
 * Provides the evaluable model related to route movement.
 */
@Getter
@RequiredArgsConstructor
public class RouteMovementEvaluableModel implements ComputatorEvaluableModel {

    private final ActorParticipant participant;
    private final GlobalMovementCalculator globalMovementCalculator;
    private final RouteMovementDirectionStrategy movementDirectionStrategy;

    @Setter
    private int repeatingMovementTime;

    @Setter
    private boolean isComputationPause;

    @Setter
    private Function<ActorParticipant, Void> onComplete;

}
