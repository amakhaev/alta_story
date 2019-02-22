package com.alta.computator.service.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.FocusPointParticipant;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the computator to calculate values for focus point participant
 */
@Slf4j
class FocusPointComputator {

    @Getter private Point constantGlobalStartCoordination;
    @Getter private final FocusPointParticipant focusPointParticipant;

    /**
     * Initialize new instance of {@link FocusPointComputator}
     */
    FocusPointComputator(FocusPointParticipant focusPointParticipant) {
        this.focusPointParticipant = focusPointParticipant;
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
