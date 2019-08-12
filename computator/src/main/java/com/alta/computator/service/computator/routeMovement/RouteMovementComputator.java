package com.alta.computator.service.computator.routeMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.computator.Computator;
import com.alta.computator.service.computator.ComputatorUtils;
import com.alta.computator.service.computator.movement.MovementWorker;
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

        if (!evaluableModel.getWorker().isCurrentlyRunning()) {
            this.calculateNextStep(evaluableModel, args);
            return;
        }

        ComputatorUtils.calculateMovementStep(
                evaluableModel.getWorker(),
                evaluableModel.getParticipant(),
                args.getAltitudeMap(),
                args.getFocusPointGlobalCoordinates()
        );
        evaluableModel.setRepeatingMovementTime(0);
        evaluableModel.getParticipant().setCurrentDirection(evaluableModel.getMovementDirectionStrategy().getDirection());
    }

    private void calculateNextStep(RouteMovementEvaluableModel evaluableModel, RouteMovementArgs args) {
        if (!evaluableModel.getMovementDirectionStrategy().isRouteCompleted()) {
            this.continueAlongRoute(evaluableModel, args);
            return;
        }

        ComputatorUtils.calculateGlobalCoordinates(
                evaluableModel.getParticipant(), args.getAltitudeMap(), args.getFocusPointGlobalCoordinates()
        );
        evaluableModel.setRepeatingMovementTime(evaluableModel.getRepeatingMovementTime() + args.getDelta());
        if (evaluableModel.getRepeatingMovementTime() <= evaluableModel.getParticipant().getRepeatingMovementDurationTime()) {
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
                    evaluableModel.getWorker(), evaluableModel.getParticipant(), targetMapPoint, args.getAltitudeMap()
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

        if (!evaluableModel.isComputationPause() && !evaluableModel.getWorker().isCurrentlyRunning()) {
            this.tryToRunMovement(
                    evaluableModel.getWorker(), evaluableModel.getParticipant(), targetMapPoint, args.getAltitudeMap()
            );
        }
        evaluableModel.getParticipant().setMoving(true);
    }

    private void tryToRunMovement(MovementWorker movementWorker,
                                  ActorParticipant participant,
                                  Point targetMapPoint,
                                  AltitudeMap altitudeMap) {
        movementWorker.tryToRunMoveProcess(
                altitudeMap,
                participant.getCurrentMapCoordinates(),
                targetMapPoint
        );
        altitudeMap.setTileState(targetMapPoint.x, targetMapPoint.y, TileState.BARRIER);
    }
}
