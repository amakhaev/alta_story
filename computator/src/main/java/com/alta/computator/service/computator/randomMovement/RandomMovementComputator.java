package com.alta.computator.service.computator.randomMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.computator.Computator;
import com.alta.computator.service.computator.ComputatorUtils;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirectionStrategy;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the implementation of {@link Computator}.
 */
@Slf4j
public class RandomMovementComputator implements Computator<RandomMovementEvaluableModel, RandomMovementArgs> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(RandomMovementEvaluableModel evaluableModel, RandomMovementArgs randomMovementArgs) {
        ComputatorUtils.initializeParticipant(
                randomMovementArgs.getAltitudeMap(),
                evaluableModel.getParticipant(),
                randomMovementArgs.getFocusPointGlobalCoordinates()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compute(RandomMovementEvaluableModel evaluableModel, RandomMovementArgs args) {
        if (evaluableModel == null || args == null) {
            return;
        }

        if (evaluableModel.getGlobalMovementCalculator().isCurrentlyRunning()) {
            ComputatorUtils.calculateGlobalMovementStep(
                    evaluableModel.getGlobalMovementCalculator(),
                    evaluableModel.getParticipant(),
                    args.getAltitudeMap(),
                    args.getFocusPointGlobalCoordinates()
            );
            evaluableModel.setRepeatingMovementTime(0);
        } else {
            ComputatorUtils.calculateGlobalCoordinates(
                    evaluableModel.getParticipant(), args.getAltitudeMap(), args.getFocusPointGlobalCoordinates()
            );
            evaluableModel.setRepeatingMovementTime(evaluableModel.getRepeatingMovementTime() + args.getDelta());
            if (evaluableModel.getRepeatingMovementTime() > evaluableModel.getParticipant().getRepeatingMovementDurationTime()) {
                this.tryToRunMovement(evaluableModel, args);
                evaluableModel.setRepeatingMovementTime(0);
            }
        }
    }

    private void tryToRunMovement(RandomMovementEvaluableModel evaluableModel, RandomMovementArgs args) {
        if (evaluableModel.isComputationPause()) {
            return;
        }

        this.calculateDirection(
                evaluableModel.getMovementDirectionStrategy(), evaluableModel.getParticipant(), args.getAltitudeMap()
        );
        Point targetMapPoint = evaluableModel.getMovementDirectionStrategy().getTargetPointForMoving();

        if (evaluableModel.getGlobalMovementCalculator().isCurrentlyRunning() ||
                evaluableModel.isComputationPause() ||
                !evaluableModel.getMovementDirectionStrategy().isCanMoveTo(targetMapPoint, args.getAltitudeMap())) {
            evaluableModel.setRepeatingMovementTime(0);
            return;
        }

        evaluableModel.getGlobalMovementCalculator().tryToRunMoveProcess(
                args.getAltitudeMap(),
                evaluableModel.getParticipant().getCurrentMapCoordinates(),
                targetMapPoint
        );
        args.getAltitudeMap().setTileState(targetMapPoint.x, targetMapPoint.y, TileState.BARRIER);
        evaluableModel.getParticipant().setMoving(true);
    }

    private void calculateDirection(MovementDirectionStrategy movementDirectionStrategy,
                                    ActorParticipant participant,
                                    AltitudeMap altitudeMap) {
        movementDirectionStrategy.calculateMovement(participant.getCurrentMapCoordinates(), null, altitudeMap);
        MovementDirection direction = movementDirectionStrategy.getDirection();
        if (direction == null) {
            return;
        }
        participant.setCurrentDirection(direction);
    }
}
