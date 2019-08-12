package com.alta.computator.service.facilityMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import lombok.NonNull;

import java.awt.*;

/**
 * Provides the manager to make computations related to map.
 */
public class FacilityMovementManager {

    private final FacilityComputator facilityComputator;

    /**
     * Initialize new instance of {@link FacilityMovementManager}.
     */
    public FacilityMovementManager() {
        facilityComputator = new FacilityComputator();
    }

    /**
     * Adds the coordinated participant for calculate coordinates of it.
     *
     * @param facilityParticipant - the facility participant.
     */
    public void addFacilities(FacilityParticipant facilityParticipant) {
        this.facilityComputator.add(facilityParticipant);
    }

    /**
     * Handles the computing of map map objects
     *
     * @param altitudeMap - the altitude map
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     */
    public void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        this.facilityComputator.onCompute(altitudeMap, focusPointGlobalCoordinates);
    }

    /**
     * Finds the facility that has given map coordinates.
     *
     * @param mapCoordinates - the map coordinates for searching.
     * @return the {@link TargetedParticipantSummary} instance of null if not found.
     */
    public TargetedParticipantSummary findFacilityByMapCoordinates(@NonNull Point mapCoordinates) {
        return this.facilityComputator.findFacilityTargetByMapCoordinates(mapCoordinates);
    }

    /**
     * Finds the facility participant by given uuid.
     *
     * @param uuid - the uuid of facility participant.
     * @return the {@link FacilityParticipant} instance of null if not found.
     */
    public FacilityParticipant findFacilityByUuid(String uuid) {
        return this.facilityComputator.findParticipantByUuid(uuid);
    }

    /**
     * Removes the facility participant by given uuid.
     *
     * @param uuid - the uuid of facility to be removed.
     */
    public void removeFacility(String uuid) {
        this.facilityComputator.removeFacility(uuid);
    }
}
