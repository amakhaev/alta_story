package com.alta.computator.calculator.actingCharacter;

import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides methods for calculate coordinates of acting character.
 */
@Slf4j
@UtilityClass
public class ActingCharacterCalculator {

    /**
     * Moves the participant to focus point.
     *
     * @param participant               - the participant to be moved.
     * @param altitudeMap               - the {@link AltitudeMap} instance.
     * @param focusPointMapCoordinates  - the focus point coordinates.
     * @return true if participant was moved to focus point, false otherwise.
     */
    boolean moveParticipantToFocusPoint(ActingCharacterParticipant participant,
                                        AltitudeMap altitudeMap,
                                        Point focusPointMapCoordinates) {
        if (altitudeMap == null || participant == null || focusPointMapCoordinates == null) {
            log.error("Focus point: {}, AltitudeMap: {}, Participant: {}", focusPointMapCoordinates, altitudeMap, participant);
            throw new RuntimeException("One or more required arguments are null.");
        }

        if (participant.getCurrentMapCoordinates() != null &&
                participant.getCurrentMapCoordinates().equals(focusPointMapCoordinates)) {
            return false;
        }

        updateAltitudeMap(altitudeMap, participant.getCurrentMapCoordinates(), focusPointMapCoordinates);
        participant.updateCurrentMapCoordinates(focusPointMapCoordinates.x, focusPointMapCoordinates.y);
        return true;
    }

    /**
     * Updates the coordinates of acting character.
     *
     * @param participant                   - the {@link ActingCharacterParticipant} instance.
     * @param focusPointGlobalCoordinates   - the global coordinates of focus point.
     */
    void updateParticipantCoordinates(ActingCharacterParticipant participant, Point focusPointGlobalCoordinates) {
        if (participant == null || focusPointGlobalCoordinates == null) {
            log.error("Focus point: {}, Participant: {}", focusPointGlobalCoordinates, participant);
            throw new RuntimeException("One or more required arguments are null.");
        }

        if (participant.getCurrentGlobalCoordinates() == null ||
                !participant.getCurrentGlobalCoordinates().equals(focusPointGlobalCoordinates)) {
            participant.updateCurrentGlobalCoordinates(focusPointGlobalCoordinates.x, focusPointGlobalCoordinates.y);
        }
    }

    void updateDirection(MovementDirection lastDirection,
                         MovementDirection currentDirection,
                         ActingCharacterParticipant participant) {
        if (lastDirection != null && lastDirection != currentDirection) {
            participant.setCurrentDirection(lastDirection);
        }
    }

    private void updateAltitudeMap(AltitudeMap altitudeMap, Point oldMapCoordinates, Point newMapCoordinates) {
        altitudeMap.setTileState(newMapCoordinates.x, newMapCoordinates.y, TileState.BARRIER);
        altitudeMap.setTileState(oldMapCoordinates.x, oldMapCoordinates.y, TileState.FREE);
    }
}
