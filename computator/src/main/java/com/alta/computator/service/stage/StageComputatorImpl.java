package com.alta.computator.service.stage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.model.participant.map.MapParticipant;
import com.alta.computator.service.layer.LayerComputator;
import com.alta.computator.service.movement.FacilityComputator;
import com.alta.computator.service.movement.focusPoint.FocusPointComputator;
import com.alta.computator.service.movement.MapComputator;
import com.alta.computator.service.movement.actor.ActingCharacterComputator;
import com.alta.computator.service.movement.actor.SimpleNpcListComputator;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.eventStream.EventProducer;
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
public class StageComputatorImpl implements StageComputator {

    private final LayerComputator layerComputator;
    private final FacilityComputator facilityComputator;
    private final SimpleNpcListComputator simpleNpcListComputator;

    private FocusPointComputator focusPointComputator;
    private MapComputator mapComputator;
    private ActingCharacterComputator actingCharacterComputator;

    @Setter
    @Getter
    private AltitudeMap altitudeMap;

    /**
     * Initialize new instance of {@link StageComputatorImpl}
     */
    public StageComputatorImpl() {
        this.layerComputator = new LayerComputator();
        this.facilityComputator = new FacilityComputator();
        this.simpleNpcListComputator = new SimpleNpcListComputator();
    }

    /**
     * Gets the global coordinates of focus point participant
     *
     * @return the {@link Point} or null if not exists
     */
    @Override
    public Point getMapGlobalCoordinates() {
        return this.mapComputator != null && this.mapComputator.getMapParticipant() != null ?
                this.mapComputator.getMapParticipant().getCurrentGlobalCoordinates() : null;
    }

    /**
     * Gets the list of participants in correct order for render. Order based on zIndex
     *
     * @return the {@link List} of participant.
     */
    @Override
    public List<CoordinatedParticipant> getSortedParticipants() {
        return this.layerComputator.getSortedParticipants();
    }

    /**
     * Gets the actor participant by given UUID
     *
     * @param uuid - the UUID of participant
     * @return the {@link SimpleNpcParticipant} instance of null if key not present
     */
    @Override
    public ActorParticipant getActorParticipant(String uuid) {
        if (this.actingCharacterComputator.getActingCharacterParticipant().getUuid().equals(uuid)) {
            return this.actingCharacterComputator.getActingCharacterParticipant();
        }

        return this.simpleNpcListComputator.getSimpleNpcParticipant(uuid);
    }

    /**
     * Finds the participant to which acting character is aimed now.
     *
     * @return the {@link TargetedParticipantSummary} instance or null.
     */
    @Override
    public TargetedParticipantSummary findParticipantTargetedByActingCharacter() {
        if (this.actingCharacterComputator == null || this.actingCharacterComputator.getActingCharacterParticipant() == null) {
            throw new RuntimeException("Acting character not set.");
        }

        Point targetParticipantMapCoordinates = this.actingCharacterComputator.getMapCoordinatesOfTargetParticipant();
        TargetedParticipantSummary summary = this.simpleNpcListComputator.findNpcTargetByMapCoordinates(targetParticipantMapCoordinates);
        if (summary != null) {
            return summary;
        }

        return this.facilityComputator.findFacilityTargetByMapCoordinates(targetParticipantMapCoordinates);
    }

    /**
     * Handles the next tick in the stage
     *
     * @param delta - the time between last and previous one calls
     */
    @Override
    public synchronized void onTick(int delta) {
        if (!this.isAllDataInitialized()) {
            log.warn("One or more computator model wasn't initialized. No any action will be performed");
            return;
        }

        this.focusPointComputator.onCompute(this.altitudeMap);
        this.mapComputator.onCompute(
                this.altitudeMap,
                this.focusPointComputator.getFocusPointParticipant().getCurrentGlobalCoordinates()
        );

        this.facilityComputator.onCompute(
                this.altitudeMap,
                this.focusPointComputator.getFocusPointParticipant().getCurrentGlobalCoordinates()
        );

        if (this.actingCharacterComputator != null) {
            this.actingCharacterComputator.onCompute(
                    this.altitudeMap,
                    this.focusPointComputator.getFocusPointParticipant().getCurrentMapCoordinates(),
                    this.focusPointComputator.getConstantGlobalStartCoordination(),
                    this.focusPointComputator.getLastMovementDirection(),
                    this.focusPointComputator.isMoving()
            );
        }

        this.simpleNpcListComputator.onCompute(
                this.altitudeMap,
                this.focusPointComputator.getFocusPointParticipant().getCurrentGlobalCoordinates(),
                delta
        );
    }

    /**
     * Adds the participant that presented focus point
     *
     * @param mapStartPosition - the start position of participant
     */
    public void addFocusPointParticipant(Point mapStartPosition) {
        if (mapStartPosition == null) {
            throw new RuntimeException("The coordinates of focus point is required.");
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
     * @param facilityParticipants - the list of facility participants.
     */
    public synchronized void addFacilities(List<FacilityParticipant> facilityParticipants) {
        if (facilityParticipants == null || facilityParticipants.isEmpty()) {
            log.warn("Attempt to add empty facility list.");
            return;
        }

        facilityParticipants.forEach(participant -> {
            this.facilityComputator.add(participant);
            this.layerComputator.addParticipants(participant.getFacilityPartParticipants());
        });
    }

    /**
     * Adds the acting character for computation
     *
     * @param actingCharacterParticipant - the acting haracter participant.
     */
    public void addActingCharacter(ActingCharacterParticipant actingCharacterParticipant) {
        if (actingCharacterParticipant == null) {
            log.warn("Attempt to add empty acting character.");
            return;
        }

        if (this.focusPointComputator == null) {
            log.error("The focus point is required for adding acting character");
            return;
        }

        this.actingCharacterComputator = new ActingCharacterComputator(actingCharacterParticipant);
        this.layerComputator.addParticipant(this.actingCharacterComputator.getActingCharacterParticipant());
        log.info("Added acting character to stage with UUID: {}", actingCharacterParticipant.getUuid());
    }

    /**
     * Adds the simple npc to stage for computation
     *
     * @param npcParticipants - the simple npc participants to be added for computation.
     */
    public void addSimpleNpcCharacters(List<SimpleNpcParticipant> npcParticipants) {
        if (npcParticipants == null || npcParticipants.isEmpty()) {
            log.warn("Attempt to add empty NPC character.");
            return;
        }

        npcParticipants.forEach(npcParticipant -> {
            this.simpleNpcListComputator.add(npcParticipant);
            this.layerComputator.addParticipant(npcParticipant);
            log.info("Added simple npc character to stage with UUID: {}.", npcParticipant.getUuid());
        });
    }

    /**
     * Removes the facility from computator.
     *
     * @param facilityUuid - the uuid of facility to be removed.
     */
    public synchronized void removeFacility(String facilityUuid) {
        FacilityParticipant facilityParticipant = this.facilityComputator.findParticipantByUuid(facilityUuid);
        if (facilityParticipant == null) {
            log.warn("Can't remove facility from computation. Facility with give uuid {} not found.", facilityUuid);
            return;
        }

        facilityParticipant.getFacilityPartParticipants().forEach(participantPart -> {
            this.layerComputator.removeParticipant(participantPart.getUuid());
            this.altitudeMap.setTileState(
                    participantPart.getCurrentMapCoordinates().x,
                    participantPart.getCurrentMapCoordinates().y,
                    TileState.FREE
            );
        });
        this.facilityComputator.removeFacility(facilityUuid);
    }

    /**
     * Sets the event producer for computator
     *
     * @param eventProducer - the event producer of computed model
     */
    public void setComputatorEventProducer(EventProducer<ComputatorEvent> eventProducer) {
        if (this.actingCharacterComputator == null) {
            log.warn("The acting character computator is empty. EventStream will not be set.");
        }

        this.actingCharacterComputator.setEventListener(new ActingCharacterEventListenerImpl(eventProducer));
        this.focusPointComputator.setEventListener(new FocusPointEventListenerImpl(eventProducer));
    }

    /**
     * Sets the pause on calculations process.
     *
     * @param isPause - indicates when calculation should be paused.
     */
    public void setPause(boolean isPause) {
        if (this.focusPointComputator != null) {
            this.focusPointComputator.setComputationPause(isPause);
        }

        this.simpleNpcListComputator.setPause(isPause);
    }

    /**
     * Sets the pause on movement process for character.
     *
     * @param isPause   - indicates when calculation should be paused.
     * @param uuid      - the uuid of NPC to be paused
     */
    public void setPause(boolean isPause, String uuid) {
        this.simpleNpcListComputator.setPause(isPause, uuid);
    }

    /**
     * Tries to run movement process. If process successfully ran then coordinates will update after calling onTick method
     *
     * @param movementDirection - the direction of movement
     */
    public void tryToRunMovement(MovementDirection movementDirection) {
        this.focusPointComputator.tryToRunMovement(movementDirection, this.altitudeMap);
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
