package com.alta.computator.service.stage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.model.participant.map.MapParticipant;
import com.alta.computator.service.layer.LayerComputator;
import com.alta.computator.service.movement.FacilityComputator;
import com.alta.computator.service.movement.FocusPointComputator;
import com.alta.computator.service.movement.MapComputator;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;
import java.util.UUID;

/**
 * Provides the computations that help with movement on stage e.g. frame, actors
 */
@Slf4j
public class StageComputator {

    @Setter @Getter private AltitudeMap altitudeMap;

    private FocusPointComputator focusPointComputator;
    private MapComputator mapComputator;
    private FacilityComputator facilityComputator;
    private LayerComputator layerComputator;

    /**
     * Initialize new instance of {@link LayerComputator}
     */
    public StageComputator() {
        this.layerComputator = new LayerComputator();
        this.facilityComputator = new FacilityComputator();
    }

    /**
     * Adds the participant that presented focus point
     *
     * @param mapStartPosition - the start position of participant
     */
    public void addFocusPointParticipant(Point mapStartPosition) {
        if (mapStartPosition == null) {
            throw new RuntimeException("The coordinates of focus point must be set.");
        }
        FocusPointParticipant focusPointParticipant = new FocusPointParticipant(mapStartPosition, UUID.randomUUID().toString());
        focusPointParticipant.updateCurrentMapCoordinates(mapStartPosition.x, mapStartPosition.y);
        this.focusPointComputator = new FocusPointComputator(focusPointParticipant);
        log.debug("Added focus point to stage with UUID: {}", focusPointParticipant.getUuid());

        this.mapComputator = new MapComputator(new MapParticipant(UUID.randomUUID().toString()));
        log.debug("Added map participant to stage with UUID: {}", this.mapComputator.getMapParticipant().getUuid());
    }

    /**
     * Adds the coordinated participants for calculate coordinates of movement
     *
     * @param uuid - the uuid of facility
     * @param facilityParts - the list of facility parts that should be computed
     * @param startMapCoordinates - the start coordinates of facility on map
     */
    public void addFacilities(String uuid, List<FacilityPartParticipant> facilityParts, Point startMapCoordinates) {
        if (Strings.isNullOrEmpty(uuid) || facilityParts == null || facilityParts.isEmpty() || startMapCoordinates == null) {
            log.debug("One ore more required argument not found");
            return;
        }

        FacilityParticipant participant = new FacilityParticipant(uuid, startMapCoordinates, facilityParts);
        this.facilityComputator.add(participant);
        this.layerComputator.addParticipants(participant.getFacilityPartParticipants());
    }

    /**
     * Handles the next tick in the stage
     */
    public void onTick() {
        if (!this.isAllDataInitialized()) {
            log.warn("One or more computator data wasn't initialized. No any action will be performed");
            return;
        }

        this.focusPointComputator.onCompute(this.altitudeMap);
        this.mapComputator.onCompute(
                this.altitudeMap,
                this.focusPointComputator.getFocusPointParticipant().getCurrentGlobalCoordinates()
        );

        if (this.facilityComputator != null) {
            this.facilityComputator.onCompute(
                    this.altitudeMap,
                    this.focusPointComputator.getFocusPointParticipant().getCurrentGlobalCoordinates()
            );
        }
    }

    /**
     * Tries to run movement process. If process successfully ran then coordinates will update after calling onTick method
     *
     * @param movementDirection - the direction of movement
     */
    public void tryToRunMovement(MovementDirection movementDirection) {
        this.focusPointComputator.tryToRunMovement(movementDirection, this.altitudeMap);
    }

    /**
     * Gets the global coordinates of focus point participant
     *
     * @return the {@link Point} or null if not exists
     */
    public Point getFocusPointGlobalCoordinates() {
        return this.focusPointComputator != null &&
                this.focusPointComputator.getConstantGlobalStartCoordination() != null ?
                this.focusPointComputator.getConstantGlobalStartCoordination() : null;
    }

    /**
     * Gets the global coordinates of focus point participant
     *
     * @return the {@link Point} or null if not exists
     */
    public Point getMapGlobalCoordinates() {
        return this.mapComputator != null && this.mapComputator.getMapParticipant() != null ?
                this.mapComputator.getMapParticipant().getCurrentGlobalCoordinates() : null;
    }

    /**
     * Gets the list of participants in correct order for render. Order based on zIndex
     *
     * @return the {@link List} of participant.
     */
    public List<CoordinatedParticipant> getSortedParticipants() {
        return this.layerComputator.getSortedParticipants();
    }

    private boolean isAllDataInitialized() {
        if (this.altitudeMap == null) {
            log.warn("Altitude map is not set.");
            return false;
        } else if (this.focusPointComputator == null || this.focusPointComputator.getFocusPointParticipant() == null) {
            log.warn("The focus point participant is not set.");
            return false;
        }

        return true;
    }
}
