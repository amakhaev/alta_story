package com.alta.computator.service.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;

/**
 * Provides the computator that calculates values for facilities
 */
@Slf4j
public class FacilityComputator {

    private boolean isInitializedFirstTime;
    private final List<CoordinatedParticipant> participants;

    /**
     * Initialize new instance of {@link FacilityComputator}
     */
    public FacilityComputator(List<CoordinatedParticipant> participants) {
        this.participants = participants;
        this.isInitializedFirstTime = false;
    }

    /**
     * Handles the computing of map facility objects
     *
     * @param altitudeMap - the altitude map
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     */
    void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        if (this.participants == null) {
            log.warn("No participant found");
            return;
        }

        if (!this.isInitializedFirstTime) {
            this.firstTimeUpdate(altitudeMap);
            this.isInitializedFirstTime = true;
        }

        this.participants.parallelStream().forEach(participant -> {
            int x = (altitudeMap.getScreenWidth() / 2 - altitudeMap.getTileWidth() / 2) - focusPointGlobalCoordinates.x;
            int y = (altitudeMap.getScreenHeight() / 2 - altitudeMap.getTileHeight() / 2) - focusPointGlobalCoordinates.y;
            x += MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                    altitudeMap.getTileWidth(),
                    participant.getStartMapCoordinates().x
            );

            y += MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                    altitudeMap.getTileHeight(),
                    participant.getStartMapCoordinates().y
            );

            participant.updateCurrentGlobalCoordinates(x, y);
        });
    }

    private void firstTimeUpdate(AltitudeMap altitudeMap) {
        this.participants.forEach(participant -> participant.updateStartGlobalCoordinates(
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileWidth(),
                        participant.getStartMapCoordinates().x
                ),
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileHeight(),
                        participant.getStartMapCoordinates().y
                )
        ));

        this.participants.forEach(participant -> participant.updateCurrentGlobalCoordinates(
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileWidth(),
                        participant.getStartMapCoordinates().x
                ),
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileHeight(),
                        participant.getStartMapCoordinates().y
                )
        ));
    }
}
