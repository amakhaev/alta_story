package com.alta.computator.service.participantComputator.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
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
 * Provides the calculations for list of simple NPC
 */
@Slf4j
class SimpleNpcComputator {

    private final MovementComputator movementComputator;
    private final DirectionCalculator directionCalculator;

    private boolean isInitializedFirstTime;
    private int repeatingMovementTime;

    @Setter
    private boolean isComputationPause;

    @Getter
    private final SimpleNpcParticipant simpleNpcParticipant;

    /**
     * Initialize new instance of {@link SimpleNpcComputator}
     */
    SimpleNpcComputator(SimpleNpcParticipant simpleNpcParticipant) {
        this.simpleNpcParticipant = simpleNpcParticipant;
        this.movementComputator = MovementFactory.createComputator();
        this.directionCalculator = MovementFactory.createDirectionCalculator(simpleNpcParticipant.getMovementType());
        this.isInitializedFirstTime = false;
        this.repeatingMovementTime = 0;
        this.isComputationPause = false;
    }

    /**
     * Handles the computing of coordinates for simple npc participants
     *
     * @param altitudeMap - the altitude map
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     * @param delta - the time between last and previous one calls
     */
    void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        if (this.simpleNpcParticipant == null) {
            return;
        }

        if (!this.isInitializedFirstTime) {
            this.firstTimeInitialization(altitudeMap, focusPointGlobalCoordinates);
            this.isInitializedFirstTime = true;
        }

        if (this.movementComputator.isCurrentlyRunning()) {
            this.updateRunningMovement(altitudeMap, focusPointGlobalCoordinates);
            this.repeatingMovementTime = 0;
        } else {
            this.calculateGlobalCoordinates(altitudeMap, focusPointGlobalCoordinates);
            this.repeatingMovementTime += delta;
            if (this.repeatingMovementTime > simpleNpcParticipant.getRepeatingMovementDurationTime()) {
                this.tryToRunMovement(altitudeMap);
                this.repeatingMovementTime = 0;
            }
        }
    }

    private void tryToRunMovement(AltitudeMap altitudeMap) {
        if (this.movementComputator.isCurrentlyRunning() || this.isComputationPause) {
            return;
        }

        MovementDirection direction = this.directionCalculator.getDirection();
        if (direction == null) {
            return;
        }

        Point targetMapPoint = this.directionCalculator.getTargetPointForMoving(
                direction, this.simpleNpcParticipant.getCurrentMapCoordinates()
        );

        this.simpleNpcParticipant.setCurrentDirection(direction);

        if (this.directionCalculator.isCanMoveTo(targetMapPoint, altitudeMap)) {
            this.movementComputator.tryToRunMoveProcess(
                    altitudeMap,
                    this.simpleNpcParticipant.getCurrentMapCoordinates(),
                    targetMapPoint
            );
            altitudeMap.setTileState(targetMapPoint.x, targetMapPoint.y, TileState.BARRIER);
        } else {
            this.repeatingMovementTime = 0;
        }
    }

    private void updateRunningMovement(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        this.movementComputator.onUpdate();

        // If last update complete computation then it should be cleared, otherwise just update coordinates
        if (this.movementComputator.isCurrentlyRunning()) {
            int x = this.movementComputator.getGlobalCurrentCoordinates().x +
                    MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                            altitudeMap.getScreenWidth(),
                            altitudeMap.getTileWidth(),
                            focusPointGlobalCoordinates.x
                    );

            int y = this.movementComputator.getGlobalCurrentCoordinates().y +
                    MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                            altitudeMap.getScreenHeight(),
                            altitudeMap.getTileHeight(),
                            focusPointGlobalCoordinates.y
                    );

            this.simpleNpcParticipant.updateCurrentGlobalCoordinates(x, y);
        } else {
            altitudeMap.setTileState(
                    this.simpleNpcParticipant.getCurrentMapCoordinates().x,
                    this.simpleNpcParticipant.getCurrentMapCoordinates().y,
                    TileState.FREE
            );

            this.simpleNpcParticipant.updateCurrentMapCoordinates(
                    this.movementComputator.getMapTargetCoordinates().x,
                    this.movementComputator.getMapTargetCoordinates().y
            );
            this.calculateGlobalCoordinates(altitudeMap, focusPointGlobalCoordinates);
            this.movementComputator.clearLastMovement();
        }
    }

    private void calculateGlobalCoordinates(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        int x = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenWidth(),
                altitudeMap.getTileWidth(),
                this.simpleNpcParticipant.getCurrentMapCoordinates().x,
                focusPointGlobalCoordinates.x
        );

        int y = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenHeight(),
                altitudeMap.getTileHeight(),
                this.simpleNpcParticipant.getCurrentMapCoordinates().y,
                focusPointGlobalCoordinates.y
        );

        this.simpleNpcParticipant.updateCurrentGlobalCoordinates(x, y);
    }

    private void firstTimeInitialization(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        int x = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenWidth(),
                altitudeMap.getTileWidth(),
                this.simpleNpcParticipant.getStartMapCoordinates().x,
                focusPointGlobalCoordinates.x
        );

        int y = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenHeight(),
                altitudeMap.getTileHeight(),
                this.simpleNpcParticipant.getStartMapCoordinates().y,
                focusPointGlobalCoordinates.y
        );

        this.simpleNpcParticipant.updateCurrentGlobalCoordinates(x, y);

        altitudeMap.setTileState(
                this.simpleNpcParticipant.getCurrentMapCoordinates().x,
                this.simpleNpcParticipant.getCurrentMapCoordinates().y,
                TileState.BARRIER
        );
    }
}
