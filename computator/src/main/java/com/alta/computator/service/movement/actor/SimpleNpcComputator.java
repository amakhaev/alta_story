package com.alta.computator.service.movement.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.movement.strategy.MovementStrategy;
import com.alta.computator.service.movement.strategy.MovementStrategyFactory;
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

    private final MovementStrategy movementStrategy;
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
        this.movementStrategy = MovementStrategyFactory.getStrategy(simpleNpcParticipant.getMovementStrategy());
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

        if (this.movementStrategy.isCurrentlyRunning()) {
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
        if (this.movementStrategy.isCurrentlyRunning() || this.isComputationPause) {
            return;
        }

        this.simpleNpcParticipant.setCurrentDirection(MovementDirection.randomDirection());
        Point targetMapPoint = new Point(this.simpleNpcParticipant.getCurrentMapCoordinates());
        switch (this.simpleNpcParticipant.getCurrentDirection()) {
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
                    this.simpleNpcParticipant.getCurrentMapCoordinates(),
                    targetMapPoint
            );
            altitudeMap.setTileState(targetMapPoint.x, targetMapPoint.y, TileState.BARRIER);
        } else {
            this.repeatingMovementTime = 0;
        }
    }

    private void updateRunningMovement(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        this.movementStrategy.onUpdate();

        // If last update completing movement then it should be cleared, otherwise just update coordinates
        if (this.movementStrategy.isCurrentlyRunning()) {
            int x = this.movementStrategy.getGlobalCurrentCoordinates().x +
                    MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                            altitudeMap.getScreenWidth(),
                            altitudeMap.getTileWidth(),
                            focusPointGlobalCoordinates.x
                    );

            int y = this.movementStrategy.getGlobalCurrentCoordinates().y +
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
                    this.movementStrategy.getMapTargetCoordinates().x,
                    this.movementStrategy.getMapTargetCoordinates().y
            );
            this.calculateGlobalCoordinates(altitudeMap, focusPointGlobalCoordinates);
            this.movementStrategy.clearLastMovement();
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
