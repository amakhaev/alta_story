package com.alta.computator.service.participantComputator.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.service.movement.MovementComputator;
import com.alta.computator.service.movement.MovementComputatorImpl;
import com.alta.computator.service.movement.MovementFactory;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the base computator for NPC characters.
 */
public abstract class NpcComputator {

    private final MovementComputator movementComputator;

    private boolean isInitializedFirstTime;

    protected int repeatingMovementTime;

    @Setter
    protected boolean isComputationPause;

    @Getter
    protected final NpcParticipant npcParticipant;

    /**
     * Initialize new instance of {@link NpcComputator}
     */
    NpcComputator(NpcParticipant npcParticipant, int movementSpeed) {
        this.npcParticipant = npcParticipant;
        this.isInitializedFirstTime = false;
        this.repeatingMovementTime = 0;
        this.isComputationPause = false;
        this.movementComputator = MovementFactory.createComputator(movementSpeed);
    }

    /**
     * Initialize new instance of {@link NpcComputator}.
     *
     * @param npcParticipant - the NPC participant model.
     */
    NpcComputator(NpcParticipant npcParticipant) {
        this(npcParticipant, MovementComputatorImpl.NORMAL_MOVE_SPEED);
    }

    /**
     * Handles the computing of coordinates for npc participants
     *
     * @param altitudeMap - the altitude map
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     * @param delta - the time between last and previous one calls
     */
    final void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        if (this.npcParticipant == null) {
            return;
        }

        if (!this.isInitializedFirstTime) {
            this.firstTimeInitialization(altitudeMap, focusPointGlobalCoordinates);
            this.isInitializedFirstTime = true;
        }

        if (this.movementComputator.isCurrentlyRunning()) {
            this.updateRunningMovement(altitudeMap, focusPointGlobalCoordinates);
            this.repeatingMovementTime = 0;
        } else {
            this.updateMovement(altitudeMap, focusPointGlobalCoordinates, delta);
        }
    }

    /**
     * Updates the movement of npc includes the direction.
     *
     * @param altitudeMap                   - the {@link AltitudeMap} instance.
     * @param focusPointGlobalCoordinates   - the global coordinates of focus point.
     * @param delta                         - the time between last and previous one calls.
     */
    protected abstract void updateMovement(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta);

    /**
     * Tries to run movement process for NPC participant.
     *
     * @param targetMapPoint    - the target point where NPC to be moved.
     * @param altitudeMap       - the {@link AltitudeMap} instance.
     */
    protected void tryToRunMovement(Point targetMapPoint, AltitudeMap altitudeMap) {
        if (this.movementComputator.isCurrentlyRunning() || this.isComputationPause) {
            return;
        }

        this.movementComputator.tryToRunMoveProcess(
                altitudeMap,
                this.npcParticipant.getCurrentMapCoordinates(),
                targetMapPoint
        );
        altitudeMap.setTileState(targetMapPoint.x, targetMapPoint.y, TileState.BARRIER);
    }

    /**
     * Updates the state of movement including coordinates of NPC.
     *
     * @param altitudeMap                   - the {@link AltitudeMap} instance.
     * @param focusPointGlobalCoordinates   - the global coordinates of focus point.
     */
    protected void updateRunningMovement(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        this.movementComputator.onUpdate();

        // If last update complete computation then it should be cleared, otherwise just update coordinates
        if (movementComputator.isCurrentlyRunning()) {
            int x = movementComputator.getGlobalCurrentCoordinates().x +
                    MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                            altitudeMap.getScreenWidth(),
                            altitudeMap.getTileWidth(),
                            focusPointGlobalCoordinates.x
                    );

            int y = movementComputator.getGlobalCurrentCoordinates().y +
                    MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                            altitudeMap.getScreenHeight(),
                            altitudeMap.getTileHeight(),
                            focusPointGlobalCoordinates.y
                    );

            this.npcParticipant.updateCurrentGlobalCoordinates(x, y);
        } else {
            altitudeMap.setTileState(
                    this.npcParticipant.getCurrentMapCoordinates().x,
                    this.npcParticipant.getCurrentMapCoordinates().y,
                    TileState.FREE
            );

            this.npcParticipant.updateCurrentMapCoordinates(
                    movementComputator.getMapTargetCoordinates().x,
                    movementComputator.getMapTargetCoordinates().y
            );
            this.calculateGlobalCoordinates(altitudeMap, focusPointGlobalCoordinates);
            this.movementComputator.clearLastMovement();
        }
    }

    /**
     * Calculates the global coordinates of NPC based on coordinates of focus point.
     *
     * @param altitudeMap                   - the {@link AltitudeMap} instance.
     * @param focusPointGlobalCoordinates   - the global coordinates of focus point.
     */
    protected void calculateGlobalCoordinates(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        int x = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenWidth(),
                altitudeMap.getTileWidth(),
                this.npcParticipant.getCurrentMapCoordinates().x,
                focusPointGlobalCoordinates.x
        );

        int y = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenHeight(),
                altitudeMap.getTileHeight(),
                this.npcParticipant.getCurrentMapCoordinates().y,
                focusPointGlobalCoordinates.y
        );

        this.npcParticipant.updateCurrentGlobalCoordinates(x, y);
    }

    private void firstTimeInitialization(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        int x = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenWidth(),
                altitudeMap.getTileWidth(),
                this.npcParticipant.getStartMapCoordinates().x,
                focusPointGlobalCoordinates.x
        );

        int y = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenHeight(),
                altitudeMap.getTileHeight(),
                this.npcParticipant.getStartMapCoordinates().y,
                focusPointGlobalCoordinates.y
        );

        this.npcParticipant.updateCurrentGlobalCoordinates(x, y);

        altitudeMap.setTileState(
                this.npcParticipant.getCurrentMapCoordinates().x,
                this.npcParticipant.getCurrentMapCoordinates().y,
                TileState.BARRIER
        );
    }
}
