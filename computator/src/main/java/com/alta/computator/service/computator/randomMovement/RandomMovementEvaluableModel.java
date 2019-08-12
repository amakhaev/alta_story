package com.alta.computator.service.computator.randomMovement;

import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.computator.ComputatorEvaluableModel;
import com.alta.computator.service.computator.movement.MovementWorker;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirectionStrategy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Provides the evaluable model related to random movement.
 */
@Getter
@RequiredArgsConstructor
public class RandomMovementEvaluableModel implements ComputatorEvaluableModel {

    private final ActorParticipant participant;
    private final MovementWorker worker;
    private final MovementDirectionStrategy movementDirectionStrategy;

    @Setter
    private int repeatingMovementTime;

    @Setter
    private boolean isComputationPause;

}
