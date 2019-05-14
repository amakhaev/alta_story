package com.alta.computator.service.participantComputator.focusPoint;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.service.movement.MovementType;
import com.alta.computator.service.movement.directionCalculation.DirectionCalculator;
import com.alta.computator.service.movement.directionCalculation.MovementDirection;
import com.alta.computator.service.movement.MovementComputator;
import com.alta.computator.service.movement.MovementFactory;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the computator to calculate values for focus point participant
 */
@Slf4j
public class FocusPointComputator {

    private final MovementComputator movementComputator;
    private final DirectionCalculator directionCalculator;

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
        this.movementComputator = MovementFactory.createComputator();
        this.directionCalculator = MovementFactory.createDirectionCalculator(MovementType.AVOID_OBSTRUCTION);
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

        if (this.movementComputator.isCurrentlyRunning()) {
            this.updateRunningMovement(altitudeMap);
        }
    }

    /**
     * Tries to run participantComputator process. If process successfully ran then coordinates will update after calling onTick method
     *
     * @param movementDirection - the direction of participantComputator
     * @param altitudeMap - the {@link AltitudeMap} instance
     */
    public void tryToRunMovement(MovementDirection movementDirection,
                                 AltitudeMap altitudeMap) {
        if (this.movementComputator.isCurrentlyRunning() || movementDirection == null || this.isComputationPause) {
            return;
        }

        this.lastMovementDirection = movementDirection;
        Point targetMapPoint = this.directionCalculator.getTargetPointForMoving(
                movementDirection, this.focusPointParticipant.getCurrentMapCoordinates()
        );

        if (this.directionCalculator.isCanMoveTo(targetMapPoint, altitudeMap)) {
            this.movementComputator.tryToRunMoveProcess(
                    altitudeMap,
                    this.focusPointParticipant.getCurrentMapCoordinates(),
                    targetMapPoint
            );

            if (this.eventListener != null &&
                    this.movementComputator.isCurrentlyRunning() &&
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
        return this.movementComputator.isCurrentlyRunning();
    }

    private void updateRunningMovement(AltitudeMap altitudeMap) {
        this.movementComputator.onUpdate();

        // If last update completing participantComputator then it should be cleared, otherwise just update coordinates
        if (this.movementComputator.isCurrentlyRunning()) {
            this.focusPointParticipant.updateCurrentGlobalCoordinates(
                    this.movementComputator.getGlobalCurrentCoordinates().x,
                    this.movementComputator.getGlobalCurrentCoordinates().y
            );
        } else {
            this.focusPointParticipant.updateCurrentMapCoordinates(
                    this.movementComputator.getMapTargetCoordinates().x,
                    this.movementComputator.getMapTargetCoordinates().y
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
            this.movementComputator.clearLastMovement();
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
