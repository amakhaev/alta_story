package com.alta.computator.service.movement.focusPoint;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.event.ActingCharacterJumpEvent;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.movement.strategy.MovementStrategy;
import com.alta.computator.service.movement.strategy.MovementStrategyFactory;
import com.alta.computator.utils.MovementCoordinateComputator;
import com.alta.eventStream.EventProducer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the computator to calculate values for focus point participant
 */
@Slf4j
public class FocusPointComputator {

    private final MovementStrategy movementStrategy;
    private boolean isInitializedFirstTime;

    @Setter
    private boolean isComputationPause;

    @Setter
    private FocusPointEventListener eventListener;

    @Getter
    private Point constantGlobalStartCoordination;

    @Getter
    private final FocusPointParticipant focusPointParticipant;

    @Getter
    private MovementDirection lastMovementDirection;

    /**
     * Initialize new instance of {@link FocusPointComputator}
     */
    public FocusPointComputator(FocusPointParticipant focusPointParticipant) {
        this.focusPointParticipant = focusPointParticipant;
        this.movementStrategy = MovementStrategyFactory.getStrategy(MovementStrategyFactory.Strategy.AVOID_OBSTRUCTION);
        this.isInitializedFirstTime = false;
        this.isComputationPause = false;
    }

    /**
     * Handles the computing of focus point participant
     */
    public void onCompute(AltitudeMap altitudeMap) {
        if (this.focusPointParticipant == null) {
            log.warn("Focus point participant not found");
            return;
        }

        if (!this.isInitializedFirstTime) {
            this.firstTimeUpdate(altitudeMap);
            this.isInitializedFirstTime = true;
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
        if (this.movementStrategy.isCurrentlyRunning() || movementDirection == null || this.isComputationPause) {
            return;
        }

        this.lastMovementDirection = movementDirection;
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
            this.movementStrategy.tryToRunMoveProcess(
                    altitudeMap,
                    this.focusPointParticipant.getCurrentMapCoordinates(),
                    targetMapPoint
            );

            if (this.eventListener != null &&
                    this.movementStrategy.isCurrentlyRunning() &&
                    altitudeMap.isJumpTileState(targetMapPoint.x,targetMapPoint.y)) {
                this.eventListener.onBeforeMovingToJumpTile(targetMapPoint);
            }
        }
    }

    /**
     * Indicates when move process is running
     *
     * @return true if moving between two map coordinates is running now, false otherwise.
     */
    public boolean isMoving() {
        return this.movementStrategy.isCurrentlyRunning();
    }

    private void updateRunningMovement(AltitudeMap altitudeMap) {
        this.movementStrategy.onUpdate();

        // If last update completing movement then it should be cleared, otherwise just update coordinates
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

    private void firstTimeUpdate(AltitudeMap altitudeMap) {
        this.constantGlobalStartCoordination = new Point(
                MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(
                        altitudeMap.getTileWidth(), altitudeMap.getScreenWidth()
                ),
                MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(
                        altitudeMap.getTileHeight(), altitudeMap.getScreenHeight()
                )
        );

        this.focusPointParticipant.updateCurrentGlobalCoordinates(
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileWidth(),
                        this.focusPointParticipant.getStartMapCoordinates().x
                ),
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileHeight(),
                        this.focusPointParticipant.getStartMapCoordinates().y
                )
        );
    }
}
