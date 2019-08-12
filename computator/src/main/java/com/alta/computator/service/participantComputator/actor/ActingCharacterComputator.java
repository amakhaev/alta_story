package com.alta.computator.service.participantComputator.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the computator for acting character
 */
@Slf4j
public class ActingCharacterComputator {

    @Getter
    private final ActingCharacterParticipant actingCharacterParticipant;

    @Setter
    private ActingCharacterEventListener eventListener;

    /**
     * Initialize new instance of {@link ActingCharacterComputator}
     */
    public ActingCharacterComputator(ActingCharacterParticipant participant) {
        this.actingCharacterParticipant = participant;
        this.actingCharacterParticipant.setCurrentDirection(MovementDirection.DOWN);
    }

    /**
     * Handles the computing of map map objects
     *
     * @param altitudeMap - the altitude map instance
     * @param focusPointMapCoordinates - the map coordinates of focus point
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     * @param direction - the direction of last moving.
     * @param isMoving - indicates when move process is running
     */
    public void onCompute(AltitudeMap altitudeMap,
                          Point focusPointMapCoordinates,
                          Point focusPointGlobalCoordinates,
                          MovementDirection direction,
                          boolean isMoving) {
        if (this.actingCharacterParticipant.getCurrentMapCoordinates() == null ||
                this.actingCharacterParticipant.getCurrentMapCoordinates().x != focusPointMapCoordinates.x ||
                this.actingCharacterParticipant.getCurrentMapCoordinates().y != focusPointMapCoordinates.y) {
            this.updateAltitudeMap(
                    altitudeMap,
                    this.actingCharacterParticipant.getCurrentMapCoordinates(),
                    focusPointMapCoordinates
            );
            this.actingCharacterParticipant.updateCurrentMapCoordinates(
                    focusPointMapCoordinates.x,
                    focusPointMapCoordinates.y
            );
            this.produceJumpEventIfNeeded(altitudeMap);
        }

        if (this.actingCharacterParticipant.getCurrentGlobalCoordinates() == null ||
                this.actingCharacterParticipant.getCurrentGlobalCoordinates().x != focusPointGlobalCoordinates.x ||
                this.actingCharacterParticipant.getCurrentGlobalCoordinates().y != focusPointGlobalCoordinates.y) {
            this.actingCharacterParticipant.updateCurrentGlobalCoordinates(
                    focusPointGlobalCoordinates.x,
                    focusPointGlobalCoordinates.y
            );
        }

        if (direction != null && this.actingCharacterParticipant.getCurrentDirection() != direction) {
            this.actingCharacterParticipant.setCurrentDirection(direction);
        }
        this.actingCharacterParticipant.setMoving(isMoving);
    }

    /**
     * Finds the coordinates of target participant to which acting character is aimed
     *
     * @return the coordinates of target participant.
     */
    public Point getMapCoordinatesOfTargetParticipant() {
        Point actingCharacterMapCoordinates = actingCharacterParticipant.getCurrentMapCoordinates();

        Point targetCharacterMapCoordinate = new Point(actingCharacterMapCoordinates);
        if (this.actingCharacterParticipant.getCurrentDirection() == null) {
            this.actingCharacterParticipant.setCurrentDirection(MovementDirection.DOWN);
        }

        switch (this.actingCharacterParticipant.getCurrentDirection()) {
            case UP:
                targetCharacterMapCoordinate.y--;
                break;
            case DOWN:
                targetCharacterMapCoordinate.y++;
                break;
            case LEFT:
                targetCharacterMapCoordinate.x--;
                break;
            case RIGHT:
                targetCharacterMapCoordinate.x++;
                break;
        }

        return targetCharacterMapCoordinate;
    }

    private void updateAltitudeMap(AltitudeMap altitudeMap, Point oldMapCoordinates, Point newMapCoordinates) {
        altitudeMap.setTileState(newMapCoordinates.x, newMapCoordinates.y, TileState.BARRIER);
        altitudeMap.setTileState(oldMapCoordinates.x, oldMapCoordinates.y, TileState.FREE);
    }

    private void produceJumpEventIfNeeded(AltitudeMap altitudeMap) {
        if (this.eventListener == null ||
                !altitudeMap.isJumpTileState(
                        this.actingCharacterParticipant.getCurrentMapCoordinates().x,
                        this.actingCharacterParticipant.getCurrentMapCoordinates().y
                )) {
            return;
        }

        this.eventListener.onAfterMovingToJumpPoint(
                this.actingCharacterParticipant.getCurrentMapCoordinates(),
                this.actingCharacterParticipant.getUuid()
        );
    }
}
