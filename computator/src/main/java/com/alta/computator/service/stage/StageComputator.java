package com.alta.computator.service.stage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import com.alta.computator.model.participant.facility.FacilityPartParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.model.participant.map.MapParticipant;
import com.alta.computator.service.layer.LayerComputator;
import com.alta.computator.service.movement.actor.ActingCharacterComputator;
import com.alta.computator.service.movement.FacilityComputator;
import com.alta.computator.service.movement.FocusPointComputator;
import com.alta.computator.service.movement.MapComputator;
import com.alta.computator.service.movement.actor.SimpleNpcListComputator;
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

    @Setter
    @Getter
    private AltitudeMap altitudeMap;

    private FocusPointComputator focusPointComputator;
    private MapComputator mapComputator;
    private FacilityComputator facilityComputator;
    private SimpleNpcListComputator simpleNpcListComputator;
    private ActingCharacterComputator actingCharacterComputator;
    private LayerComputator layerComputator;

    /**
     * Initialize new instance of {@link LayerComputator}
     */
    public StageComputator() {
        this.layerComputator = new LayerComputator();
        this.facilityComputator = new FacilityComputator();
        this.simpleNpcListComputator = new SimpleNpcListComputator();
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
        log.info("Added focus point to stage with UUID: {}", focusPointParticipant.getUuid());

        this.mapComputator = new MapComputator(new MapParticipant(UUID.randomUUID().toString()));
        log.info("Added map participant to stage with UUID: {}", this.mapComputator.getMapParticipant().getUuid());
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
     * Adds the acting character for computation
     *
     * @param uuid - the uuid of character
     * @param mapStartPosition - the start coordinates of character on map
     * @param zIndex - the z-index of character
     */
    public void addActingCharacter(String uuid, Point mapStartPosition, int zIndex) {
        if (this.focusPointComputator == null) {
            log.error("The focus point is required for adding acting character");
            return;
        }

        this.actingCharacterComputator = new ActingCharacterComputator(
                new ActingCharacterParticipant(uuid, mapStartPosition, zIndex)
        );
        this.layerComputator.addParticipant(this.actingCharacterComputator.getActingCharacterParticipant());
        log.info("Added acting character to stage with UUID: {}", uuid);
    }

    /**
     * Adds the simple npc to stage for computation
     *
     * @param uuid = the uuid of npc
     * @param mapStartPosition - the start coordinates of npc on map
     * @param zIndex - the z-index of character
     * @param repeatingMovementDurationTime - the time interval between movements
     */
    public void addSimpleNpcCharacter(String uuid, Point mapStartPosition, int zIndex, int repeatingMovementDurationTime) {
        SimpleNpcParticipant npcParticipant = new SimpleNpcParticipant(
                uuid,
                mapStartPosition,
                zIndex,
                repeatingMovementDurationTime
        );

        this.simpleNpcListComputator.add(npcParticipant);
        this.layerComputator.addParticipant(npcParticipant);
        log.info("Added simple npc character to stage with UUID: {}.", npcParticipant.getUuid());
    }

    /**
     * Handles the next tick in the stage
     *
     * @param delta - the time between last and previous one calls
     */
    public synchronized void onTick(int delta) {
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

        if (this.actingCharacterComputator != null) {
            this.actingCharacterComputator.onCompute(
                    this.altitudeMap,
                    this.focusPointComputator.getFocusPointParticipant().getCurrentMapCoordinates(),
                    this.focusPointComputator.getConstantGlobalStartCoordination(),
                    this.focusPointComputator.getLastMovementDirection(),
                    this.focusPointComputator.isMoving()
            );
        }

        if (this.simpleNpcListComputator != null) {
            this.simpleNpcListComputator.onCompute(
                    this.altitudeMap,
                    this.focusPointComputator.getFocusPointParticipant().getCurrentGlobalCoordinates(),
                    delta
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

    /**
     * Gets the actor participant by given UUID
     *
     * @param uuid - the UUID of participant
     * @return the {@link SimpleNpcParticipant} instance of null if key not present
     */
    public ActorParticipant getActorParticipant(String uuid) {
        if (this.actingCharacterComputator.getActingCharacterParticipant().getUuid().equals(uuid)) {
            return this.actingCharacterComputator.getActingCharacterParticipant();
        }

        return this.simpleNpcListComputator.getSimpleNpcParticipant(uuid);
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