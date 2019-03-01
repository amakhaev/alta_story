package com.alta.computator.service.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the computator that calculates values for facilities
 */
@Slf4j
public class FacilityComputator {

    private boolean isInitializedFirstTime;

    @Getter
    private final List<FacilityParticipant> facilityParticipants;

    /**
     * Initialize new instance of {@link FacilityComputator}
     */
    public FacilityComputator() {
        this.facilityParticipants = new ArrayList<>();
        this.isInitializedFirstTime = false;
    }

    /**
     * Adds the facility participant to list
     *
     * @param facilityParticipant - the new participant of computation
     */
    public void add(FacilityParticipant facilityParticipant) {
        this.facilityParticipants.add(facilityParticipant);
    }

    /**
     * Handles the computing of map facility objects
     *
     * @param altitudeMap - the altitude map
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     */
    public void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        if (this.facilityParticipants.isEmpty()) {
            return;
        }

        this.facilityParticipants.forEach(
                participant -> this.calculate(participant, altitudeMap, focusPointGlobalCoordinates)
        );
    }

    private void calculate(FacilityParticipant facilityParticipant, AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        if (facilityParticipant == null) {
            return;
        }

        /*if (!this.isInitializedFirstTime) {
            this.firstTimeUpdate(altitudeMap);
            this.isInitializedFirstTime = true;
        }*/

        facilityParticipant.getFacilityPartParticipants().parallelStream().forEach(participant -> {
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

            x += MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                    altitudeMap.getTileWidth(),
                    participant.getShiftTilePosition().x
            );

            y += MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                    altitudeMap.getTileHeight(),
                    participant.getShiftTilePosition().y
            );

            participant.updateCurrentGlobalCoordinates(x, y);
        });
    }

    private void firstTimeUpdate(AltitudeMap altitudeMap) {
        /*this.facilityParticipant.updateStartGlobalCoordinates(
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileWidth(),
                        this.facilityParticipant.getStartMapCoordinates().x
                ),
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileHeight(),
                        this.facilityParticipant.getStartMapCoordinates().y
                )
        );

        this.facilityParticipant.getFacilityPartParticipants().forEach(participant -> {
            participant.updateStartGlobalCoordinates(
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileWidth(),
                            participant.getStartMapCoordinates().x
                    ),
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileHeight(),
                            participant.getStartMapCoordinates().y
                    )
            );

            participant.updateCurrentGlobalCoordinates(
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileWidth(),
                            participant.getStartMapCoordinates().x
                    ),
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileHeight(),
                            participant.getStartMapCoordinates().y
                    )
            );
        });*/
    }
}
