package com.alta.computator.service.participantComputator;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the computator that calculates values for facilities
 */
@Slf4j
public class FacilityComputator {

    @Getter
    private final List<FacilityParticipant> facilityParticipants;
    private List<FacilityParticipant> notInitializedParticipants;

    /**
     * Initialize new instance of {@link FacilityComputator}
     */
    public FacilityComputator() {
        this.facilityParticipants = new ArrayList<>();
        this.notInitializedParticipants = new ArrayList<>();
    }

    /**
     * Adds the map participant to list
     *
     * @param facilityParticipant - the new participant of computation
     */
    public void add(FacilityParticipant facilityParticipant) {
        if (facilityParticipant == null) {
            log.error("Null reference to FacilityParticipant.");
            return;
        }
        this.notInitializedParticipants.add(facilityParticipant);
    }

    /**
     * Handles the computing of map map objects
     *
     * @param altitudeMap - the altitude map
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     */
    public void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        this.firstTimeInitialization(altitudeMap);
        if (this.facilityParticipants.isEmpty()) {
            return;
        }

        this.facilityParticipants.forEach(
                participant -> this.calculate(participant, altitudeMap, focusPointGlobalCoordinates)
        );
    }

    /**
     * Finds the facility that has given map coordinates.
     *
     * @param mapCoordinates - the map coordinates for searching.
     * @return the {@link TargetedParticipantSummary} instance of null if not found.
     */
    public TargetedParticipantSummary findFacilityTargetByMapCoordinates(@NonNull Point mapCoordinates) {
        return this.facilityParticipants.stream()
                .filter(facilityParticipant -> facilityParticipant.getShiftCoordinatesByMapCoordinates(mapCoordinates) != null)
                .findFirst()
                .map(facilityParticipant -> new TargetedParticipantSummary(
                        facilityParticipant.getUuid(),
                        mapCoordinates,
                        facilityParticipant.getParticipantType(),
                        facilityParticipant.getShiftCoordinatesByMapCoordinates(mapCoordinates)
                ))
                .orElse(null);
    }

    /**
     * Finds the facility participant by given uuid.
     *
     * @param uuid - the uuid of facility participant.
     * @return the {@link FacilityParticipant} instance of null if not found.
     */
    public FacilityParticipant findParticipantByUuid(String uuid) {
        return this.getFacilityParticipants().stream()
                .filter(participant -> participant.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes the facility participant by given uuid.
     *
     * @param uuid - the uuid of facility to be removed.
     */
    public void removeFacility(String uuid) {
        this.getFacilityParticipants().removeIf(participant -> participant.getUuid().equals(uuid));
    }

    private void calculate(FacilityParticipant facilityParticipant, AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        if (facilityParticipant == null) {
            return;
        }

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

    private void firstTimeInitialization(AltitudeMap altitudeMap) {
        if (this.notInitializedParticipants.isEmpty()) {
            return;
        }

        this.notInitializedParticipants.forEach(facilityParticipant ->
                facilityParticipant.getFacilityPartParticipants().forEach(fp -> {
                    int x = fp.getStartMapCoordinates().x + fp.getShiftTilePosition().x;
                    int y = fp.getStartMapCoordinates().y + fp.getShiftTilePosition().y;
                    altitudeMap.setTileState(x, y, fp.getTileState());
                }
        ));
        this.facilityParticipants.addAll(this.notInitializedParticipants);
        this.notInitializedParticipants.clear();

    }
}