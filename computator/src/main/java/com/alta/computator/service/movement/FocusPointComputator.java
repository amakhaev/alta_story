package com.alta.computator.service.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.FocusPointParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.movement.strategy.MovementStrategy;
import com.alta.computator.service.movement.strategy.MovementStrategyFactory;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the computator to calculate values for focus point participant
 */
@Slf4j
class FocusPointComputator {

    private final MovementStrategy movementStrategy;

    @Getter private Point constantGlobalStartCoordination;
    @Getter private final FocusPointParticipant focusPointParticipant;

    /**
     * Initialize new instance of {@link FocusPointComputator}
     */
    FocusPointComputator(FocusPointParticipant focusPointParticipant) {
        this.focusPointParticipant = focusPointParticipant;
        this.movementStrategy = MovementStrategyFactory.getStrategy(MovementStrategyFactory.Strategy.AVOID_OBSTRUCTION);
        this.focusPointParticipant.updateCurrentGlobalCoordinates(
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        32,
                        this.focusPointParticipant.getStartMapCoordinates().x
                ),
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        32,
                        this.focusPointParticipant.getStartMapCoordinates().y
                )
        );
    }

    /**
     * Handles the computing of focus point participant
     */
    void onCompute(AltitudeMap altitudeMap) {
        if (this.focusPointParticipant == null) {
            log.warn("Focus point participant not found");
            return;
        }

        if (this.constantGlobalStartCoordination == null) {
            this.constantGlobalStartCoordination = new Point(
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(
                            altitudeMap.getTileWidth(), altitudeMap.getScreenWidth()
                    ),
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(
                            altitudeMap.getTileHeight(), altitudeMap.getScreenHeight()
                    )
            );
        }

        if (this.movementStrategy.isCurrentlyRunning()) {
            this.updateRunningMovement(altitudeMap);
        }
    }

    /**
     * Tries to run movement process. If process successfully ran then coordinates will update after calling onTick method
     *
     * @param movementDirection - the direction of movement
     * @param altitudeMap - the {@link AltitudeMap} instance
     */
    public void tryToRunMovement(MovementDirection movementDirection,
                                 AltitudeMap altitudeMap) {
        if (this.movementStrategy.isCurrentlyRunning() || movementDirection == null) {
            return;
        }

        Point targetMapPoint = new Point(this.focusPointParticipant.getCurrentMapCoordinates());
        switch (movementDirection) {
            case UP:
                targetMapPoint.y--;
                break;
            case DOWN:
                targetMapPoint.y++;
                break;
            case LEFT:
                targetMapPoint.x--;
                break;
            case RIGHT:
                targetMapPoint.x++;
                break;
        }

        if (this.movementStrategy.isCanMoveTo(targetMapPoint, altitudeMap)) {
            Point targetGlobalCoordinates = new Point(
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileWidth(),
                            targetMapPoint.x
                    ),
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileHeight(),
                            targetMapPoint.y
                    )
            );

            this.movementStrategy.tryToRunMoveProcess(
                    altitudeMap,
                    this.focusPointParticipant.getCurrentGlobalCoordinates(),
                    targetMapPoint,
                    targetGlobalCoordinates
            );
        }
    }

    private void updateRunningMovement(AltitudeMap altitudeMap) {
        this.movementStrategy.onUpdate();

        // If last update completed movement then it should be cleared, otherwise just update coordinates
        if (this.movementStrategy.isCurrentlyRunning()) {
            this.focusPointParticipant.updateCurrentGlobalCoordinates(
                    this.movementStrategy.getGlobalCurrentCoordinates().x,
                    this.movementStrategy.getGlobalCurrentCoordinates().y
            );
        } else {
            this.focusPointParticipant.updateCurrentMapCoordinates(
                    this.movementStrategy.getMapTargetCoordinates().x,
                    this.movementStrategy.getMapTargetCoordinates().y
            );
            this.focusPointParticipant.updateCurrentGlobalCoordinates(
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileWidth(),
                            this.focusPointParticipant.getCurrentMapCoordinates().x
                    ),
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileHeight(),
                            this.focusPointParticipant.getCurrentMapCoordinates().y
                    )
            );
            this.movementStrategy.clearLastMovement();
        }
    }
}
