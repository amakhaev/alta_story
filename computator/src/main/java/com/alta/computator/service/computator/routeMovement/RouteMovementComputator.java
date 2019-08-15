package com.alta.computator.service.computator.routeMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.computator.Computator;
import com.alta.computator.service.computator.ComputatorUtils;
import com.alta.computator.service.computator.movement.GlobalMovementCalculator;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the implementation of {@link Computator}.
 */
@Slf4j
public class RouteMovementComputator implements Computator<RouteMovementEvaluableModel, RouteMovementArgs> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(RouteMovementEvaluableModel model, RouteMovementArgs args) {
        ComputatorUtils.initializeParticipant(
                args.getAltitudeMap(),
                model.getParticipant(),
                args.getFocusPointGlobalCoordinates()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compute(RouteMovementEvaluableModel evaluableModel, RouteMovementArgs args) {
        if (evaluableModel == null || args == null) {
            return;
        }

        if (evaluableModel.getGlobalMovementCalculator().isCurrentlyRunning()) {
            // In case if global calculator isn't working, try to run calculation based on given parameters.
            this.proceedGlobalCalculation(evaluableModel, args);
        } else {
            // In case if calculator already ran, just proceed calculation based on new parameters.
            this.tryToStartGlobalCalculation(evaluableModel, args);
        }

        // Try to detect when route was completed.
        if (evaluableModel.getOnComplete() != null &&
                evaluableModel.getMovementDirectionStrategy().isRouteCompleted() &&
                !evaluableModel.getGlobalMovementCalculator().isCurrentlyRunning()) {
            evaluableModel.getOnComplete().apply(evaluableModel.getParticipant());
        }
    }

    private void proceedGlobalCalculation(RouteMovementEvaluableModel evaluableModel, RouteMovementArgs args) {
        ComputatorUtils.calculateGlobalMovementStep(
                evaluableModel.getGlobalMovementCalculator(),
                evaluableModel.getParticipant(),
                args.getAltitudeMap(),
                args.getFocusPointGlobalCoordinates()
        );
        evaluableModel.setRepeatingMovementTime(0);

        // The running process can be finished in ComputatorUtils.calculateMovementStep.
        // In that case direction should be changed.
        if (!evaluableModel.getGlobalMovementCalculator().isCurrentlyRunning()) {
            evaluableModel.getParticipant().setCurrentDirection(evaluableModel.getMovementDirectionStrategy().getDirection());
        }
    }

    private void tryToStartGlobalCalculation(RouteMovementEvaluableModel evaluableModel, RouteMovementArgs args) {
        if (!evaluableModel.getMovementDirectionStrategy().isRouteCompleted()) {
            this.continueAlongRoute(evaluableModel, args);
            return;
        }

        ComputatorUtils.calculateGlobalCoordinates(
                evaluableModel.getParticipant(), args.getAltitudeMap(), args.getFocusPointGlobalCoordinates()
        );
        evaluableModel.setRepeatingMovementTime(evaluableModel.getRepeatingMovementTime() + args.getDelta());
        if (evaluableModel.getRepeatingMovementTime() <= evaluableModel.getParticipant().getRepeatingMovementDurationTime() &&
                !args.isRunImmediately()) {
            return;
        }

        evaluableModel.getMovementDirectionStrategy().calculateMovement(
                evaluableModel.getParticipant().getCurrentMapCoordinates(), null, args.getAltitudeMap()
        );
        this.tryToRunMovementForRoute(evaluableModel, args);
        evaluableModel.setRepeatingMovementTime(0);
    }

    private void tryToRunMovementForRoute(RouteMovementEvaluableModel evaluableModel, RouteMovementArgs args) {
        if (evaluableModel.isComputationPause()) {
            return;
        }

        MovementDirection direction = evaluableModel.getMovementDirectionStrategy().getDirection();
        if (direction == null) {
            return;
        }

        Point targetMapPoint = evaluableModel.getMovementDirectionStrategy().getTargetPointForMoving();
        evaluableModel.getParticipant().setCurrentDirection(direction);

        if (evaluableModel.getMovementDirectionStrategy().isCanMoveTo(targetMapPoint, args.getAltitudeMap())) {
            this.tryToRunMovement(
                    evaluableModel.getGlobalMovementCalculator(), evaluableModel.getParticipant(), targetMapPoint, args.getAltitudeMap()
            );
            evaluableModel.getParticipant().setMoving(true);
        } else {
            evaluableModel.setRepeatingMovementTime(0);
        }
    }

    private void continueAlongRoute(RouteMovementEvaluableModel evaluableModel, RouteMovementArgs args) {
        if (evaluableModel.isComputationPause()) {
            return;
        }

        MovementDirection direction = evaluableModel.getMovementDirectionStrategy().getDirection();
        if (direction == null) {
            return;
        }

        Point targetMapPoint;
        do {
            targetMapPoint = evaluableModel.getMovementDirectionStrategy().getTargetPointForMoving();
        } while (targetMapPoint != null && targetMapPoint.equals(evaluableModel.getParticipant().getCurrentMapCoordinates()));
        evaluableModel.getParticipant().setCurrentDirection(direction);

        if (!evaluableModel.getMovementDirectionStrategy().isCanMoveTo(targetMapPoint, args.getAltitudeMap())) {
            evaluableModel.getMovementDirectionStrategy().recalculatePartOfRoute(
                    evaluableModel.getParticipant().getCurrentMapCoordinates(),
                    args.getAltitudeMap()
            );
            return;
        }

        if (!evaluableModel.isComputationPause() && !evaluableModel.getGlobalMovementCalculator().isCurrentlyRunning()) {
            this.tryToRunMovement(
                    evaluableModel.getGlobalMovementCalculator(),
                    evaluableModel.getParticipant(),
                    targetMapPoint,
                    args.getAltitudeMap()
            );
        }
        evaluableModel.getParticipant().setMoving(true);
    }

    private void tryToRunMovement(GlobalMovementCalculator globalMovementCalculator,
                                  ActorParticipant participant,
                                  Point targetMapPoint,
                                  AltitudeMap altitudeMap) {
        globalMovementCalculator.tryToRunMoveProcess(
                altitudeMap,
                participant.getCurrentMapCoordinates(),
                targetMapPoint
        );
        altitudeMap.setTileState(targetMapPoint.x, targetMapPoint.y, TileState.BARRIER);
    }
}
