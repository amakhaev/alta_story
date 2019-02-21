package com.alta.computator.service.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.MovableParticipant;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.Getter;

/**
 * Provides the computator to calculate values for focus point participant
 */
class FocusPointComputator {

    @Getter
    private final MovableParticipant focusPointParticipant;

    /**
     * Initialize new instance of {@link FocusPointComputator}
     */
    FocusPointComputator(MovableParticipant focusPointParticipant) {
        this.focusPointParticipant = focusPointParticipant;
    }

    /**
     * Handles the computing of focus point participant
     */
    public void onCompute(AltitudeMap altitudeMap) {
        if (this.focusPointParticipant == null) {
            return;
        }

        this.focusPointParticipant.updateCurrentGlobalCoordinates(
                MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(
                        altitudeMap.getTileWidth(), altitudeMap.getScreenWidth()
                ),
                MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(
                        altitudeMap.getTileHeight(), altitudeMap.getScreenHeight()
                )
        );
    }
}
