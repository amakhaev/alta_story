package com.alta.computator.service.movement.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the computator for acting character
 */
public class ActingCharacterComputator {

    @Getter
    private final ActingCharacterParticipant actingCharacterParticipant;

    /**
     * Initialize new instance of {@link ActingCharacterComputator}
     */
    public ActingCharacterComputator(ActingCharacterParticipant participant) {
        this.actingCharacterParticipant = participant;
        this.actingCharacterParticipant.setCurrentDirection(MovementDirection.DOWN);
    }

    /**
     * Handles the computing of map facility objects
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
        }

        if (this.actingCharacterParticipant.getCurrentGlobalCoordinates() == null ||
                this.actingCharacterParticipant.getCurrentGlobalCoordinates().x != focusPointGlobalCoordinates.x ||
                this.actingCharacterParticipant.getCurrentGlobalCoordinates().y != focusPointGlobalCoordinates.y) {
            this.actingCharacterParticipant.updateCurrentGlobalCoordinates(
                    focusPointGlobalCoordinates.x,
                    focusPointGlobalCoordinates.y
            );
        }

        if (this.actingCharacterParticipant.getCurrentDirection() != direction) {
            this.actingCharacterParticipant.setCurrentDirection(direction);
        }
        this.actingCharacterParticipant.setMoving(isMoving);
    }

    private void updateAltitudeMap(AltitudeMap altitudeMap, Point oldMapCoordinates, Point newMapCoordinates) {
        altitudeMap.setTileState(newMapCoordinates.x, newMapCoordinates.y, TileState.BARRIER);
        altitudeMap.setTileState(oldMapCoordinates.x, oldMapCoordinates.y, TileState.FREE);
    }
}
