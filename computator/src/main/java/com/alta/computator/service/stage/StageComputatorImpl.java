package com.alta.computator.service.stage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.service.actingCharacterMovement.ActingCharacterMovementManager;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.service.facilityMovement.FacilityMovementManager;
import com.alta.computator.service.layer.LayerComputator;
import com.alta.computator.service.mapMovement.MapMovementManager;
import com.alta.computator.service.npcMovement.NpcMovementManager;
import com.alta.eventStream.EventProducer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;

/**
 * Provides the computations that help with participantComputator on stage e.g. frame, actors
 */
@Slf4j
public class StageComputatorImpl implements StageComputator {

    private final LayerComputator layerComputator;
    private final NpcMovementManager npcMovementManager;
    private final FacilityMovementManager facilityMovementManager;

    private MapMovementManager mapMovementManager;
    private ActingCharacterMovementManager actingCharacterMovementManager;

    @Setter
    private AltitudeMap altitudeMap;

    /**
     * Initialize new instance of {@link StageComputatorImpl}
     */
    public StageComputatorImpl() {
        this.layerComputator = new LayerComputator();
        this.npcMovementManager = new NpcMovementManager();
        this.facilityMovementManager = new FacilityMovementManager();
    }

    /**
     * Gets the global coordinates of focus point participant
     *
     * @return the {@link Point} or null if not exists
     */
    @Override
    public Point getMapGlobalCoordinates() {
        return this.mapMovementManager == null ? null : this.mapMovementManager.getMapGlobalCoordinates();
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
        if (this.actingCharacterMovementManager.getActingCharacterParticipant().getUuid().equals(uuid)) {
            return this.actingCharacterMovementManager.getActingCharacterParticipant();
        }

        return this.npcMovementManager.getParticipant(uuid);
    }

    /**
     * Finds the participant to which acting character is aimed now.
     *
     * @return the {@link TargetedParticipantSummary} instance or null.
     */
    @Override
    public TargetedParticipantSummary findParticipantTargetedByActingCharacter() {
        if (this.actingCharacterMovementManager == null ||
                this.actingCharacterMovementManager.getActingCharacterParticipant() == null) {
            throw new RuntimeException("Acting character is empty.");
        }

        Point targetParticipantMapCoordinates = this.actingCharacterMovementManager.getMapCoordinatesOfTargetParticipant();
        TargetedParticipantSummary summary = this.npcMovementManager.findNpcTargetByMapCoordinates(targetParticipantMapCoordinates);
        if (summary != null) {
            return summary;
        }

        return this.facilityMovementManager.findFacilityByMapCoordinates(targetParticipantMapCoordinates);
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

        this.mapMovementManager.onCompute(this.altitudeMap);
        this.facilityMovementManager.onCompute(this.altitudeMap, this.mapMovementManager.getFocusPointGlobalCoordinates());

        if (this.actingCharacterMovementManager != null) {
            this.actingCharacterMovementManager.onCompute(
                    this.altitudeMap,
                    this.mapMovementManager.getFocusPointMapCoordinates(),
                    this.mapMovementManager.getFocusPointGlobalStartCoordinates(),
                    this.mapMovementManager.getFocusPointLastMovementDirection(),
                    this.mapMovementManager.isFocusPointMoving()
            );
        }

        this.npcMovementManager.onCompute(
                this.altitudeMap, this.mapMovementManager.getFocusPointGlobalCoordinates(), delta
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
        this.mapMovementManager = new MapMovementManager(mapStartPosition);
    }

    /**
     * Adds the coordinated participants for calculate coordinates of participantComputator
     *
     * @param facilityParticipants - the list of facility participants.
     */
    public synchronized void addFacilities(List<FacilityParticipant> facilityParticipants) {
        if (facilityParticipants == null || facilityParticipants.isEmpty()) {
            log.warn("Attempt to add empty facility list.");
            return;
        }

        facilityParticipants.forEach(participant -> {
            this.facilityMovementManager.addFacilities(participant);
            this.layerComputator.addParticipants(participant.getFacilityPartParticipants());
        });
    }

    /**
     * Adds the acting character for computation
     *
     * @param actingCharacterParticipant - the acting character participant.
     */
    public void addActingCharacter(ActingCharacterParticipant actingCharacterParticipant) {
        if (actingCharacterParticipant == null) {
            log.warn("Attempt to add empty acting character.");
            return;
        }

        if (this.mapMovementManager == null) {
            log.error("The focus point is required for adding acting character");
            return;
        }

        this.actingCharacterMovementManager = new ActingCharacterMovementManager(actingCharacterParticipant);
        this.layerComputator.addParticipant(this.actingCharacterMovementManager.getActingCharacterParticipant());
        log.info("Added acting character to stage with UUID: {}", actingCharacterParticipant.getUuid());
    }

    /**
     * Adds the simple npcMovement to stage for computation
     *
     * @param npcParticipants - the npcMovement participants to be added for computation.
     */
    public void addNpcCharacters(List<NpcParticipant> npcParticipants) {
        if (npcParticipants == null || npcParticipants.isEmpty()) {
            log.warn("Attempt to add empty NPC character.");
            return;
        }

        npcParticipants.forEach(npcParticipant -> {
            this.npcMovementManager.addParticipantForComputation(npcParticipant);
            this.layerComputator.addParticipant(npcParticipant);
            log.debug("Added simple npcMovement character to stage with UUID: {}.", npcParticipant.getUuid());
        });
    }

    /**
     * Removes the facility from computator.
     *
     * @param facilityUuid - the uuid of facility to be removed.
     */
    public synchronized void removeFacility(String facilityUuid) {
        FacilityParticipant facilityParticipant = this.facilityMovementManager.findFacilityByUuid(facilityUuid);
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
        this.facilityMovementManager.removeFacility(facilityUuid);
    }

    /**
     * Sets the event producer for computator
     *
     * @param eventProducer - the event producer of computed model
     */
    public void setComputatorEventProducer(EventProducer<ComputatorEvent> eventProducer) {
        if (this.actingCharacterMovementManager == null) {
            log.warn("The acting character computator is empty. EventStream will not be set.");
        }

        this.actingCharacterMovementManager.setEventListener(new ActingCharacterEventListenerImpl(eventProducer));
        this.mapMovementManager.setFocusPointEventListener(new FocusPointEventListenerImpl(eventProducer));
    }

    /**
     * Sets the pause on calculations process.
     *
     * @param isPause - indicates when calculation should be paused.
     */
    public void setPause(boolean isPause) {
        if (this.mapMovementManager != null) {
            this.mapMovementManager.setPause(isPause);
        }

        this.npcMovementManager.setPause(isPause);
    }

    /**
     * Sets the pause on participantComputator process for character.
     *
     * @param isPause   - indicates when calculation should be paused.
     * @param uuid      - the uuid of NPC to be paused
     */
    public void setPause(boolean isPause, String uuid) {
        this.npcMovementManager.setPause(isPause, uuid);
    }

    /**
     * Tries to run movement process. If process successfully ran then coordinates will update after calling onTick method
     *
     * @param movementDirection - the direction of participant
     */
    public void tryToRunMovement(MovementDirection movementDirection) {
        this.mapMovementManager.tryToRunMovement(movementDirection, this.altitudeMap);
    }

    /**
     * Tries to run movement for NPC participant.
     *
     * @param npcTargetUuid - the NPC uuid.
     * @param x             - the X coordinate to be moved.
     * @param y             - the Y coordinate to be moved.
     */
    public void tryToRunNpcMovement(String npcTargetUuid, int x, int y) {
        if (this.altitudeMap == null) {
            log.warn("The movement for NPC failed since altitude map is null");
            return;
        }

        //this.npcListComputator.tryToRunRouteMovementOnceTime(this.altitudeMap, npcTargetUuid, x, y);
    }

    private boolean isAllDataInitialized() {
        if (this.altitudeMap == null) {
            log.warn("Altitude map is not set.");
            return false;
        } else if (this.mapMovementManager == null) {
            log.warn("The focus point participant is not set.");
            return false;
        }

        return true;
    }
}
