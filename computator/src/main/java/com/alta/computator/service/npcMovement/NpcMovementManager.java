package com.alta.computator.service.npcMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.service.computator.movement.MovementFactory;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.service.computator.movement.directionCalculation.RouteMovementDescription;
import com.alta.computator.service.computator.randomMovement.RandomMovementEvaluableModel;
import com.alta.computator.service.computator.routeMovement.RouteMovementEvaluableModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Collections;

/**
 * Provides the manager make computations related to NPC.
 */
@Slf4j
public class NpcMovementManager {

    private final NpcMovementService<RandomMovementEvaluableModel> simpleNpcMovementService;
    private final NpcMovementService<RouteMovementEvaluableModel> routeNpcMovementService;
    private final NpcMovementService<RouteMovementEvaluableModel> customRouteNpcMovementService;

    /**
     * Initialize new instance of {@link NpcMovementManager}.
     */
    public NpcMovementManager() {
        this.simpleNpcMovementService = new SimpleNpcMovementService();
        this.routeNpcMovementService = new RouteNpcMovementService();
        this.customRouteNpcMovementService = new RouteNpcMovementService();
    }

    /**
     * Adds the participant into container to be used for future computations.
     *
     * @param npcParticipant - the npcMovement participant to be added.
     */
    public void addParticipantForComputation(NpcParticipant npcParticipant) {
        if (npcParticipant == null) {
            log.warn("Null reference to npcMovement participant");
            return;
        }

        if (npcParticipant.getParticipantType() == ParticipatType.SIMPLE_NPC) {
            this.simpleNpcMovementService.addParticipant(npcParticipant);
        } else if (npcParticipant.getParticipantType() == ParticipatType.ROUTE_NPC) {
            this.routeNpcMovementService.addParticipant(npcParticipant);
        }
    }

    /**
     * Handles the computing of coordinates for npcMovement participants.
     *
     * @param altitudeMap                   - the altitude map
     * @param focusPointGlobalCoordinates   - the global coordinates of focus point
     * @param delta                         - the time between last and previous one calls
     */
    public synchronized void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        this.simpleNpcMovementService.onCompute(altitudeMap, focusPointGlobalCoordinates, delta);
        this.routeNpcMovementService.onCompute(altitudeMap, focusPointGlobalCoordinates, delta);
        this.customRouteNpcMovementService.onComputeImmediately(altitudeMap, focusPointGlobalCoordinates, delta);
    }

    /**
     * Gets the participant by given UUID.
     *
     * @param uuid - the UUID of participant to be found.
     * @return the {@link ActorParticipant} instance.
     */
    public ActorParticipant getParticipant(String uuid) {
        if (this.simpleNpcMovementService.hasEvaluableModel(uuid)) {
            return this.simpleNpcMovementService.getEvaluableModel(uuid).getParticipant();
        } else if (this.routeNpcMovementService.hasEvaluableModel(uuid)) {
            return this.routeNpcMovementService.getEvaluableModel(uuid).getParticipant();
        } else if (this.customRouteNpcMovementService.hasEvaluableModel(uuid)) {
            return this.customRouteNpcMovementService.getEvaluableModel(uuid).getParticipant();
        } else {
            return null;
        }
    }

    /**
     * Finds the NPC that has given map coordinates.
     *
     * @param mapCoordinates - the map coordinates for searching.
     * @return the {@link TargetedParticipantSummary} instance of null if not found.
     */
    public TargetedParticipantSummary findNpcTargetByMapCoordinates(@NonNull Point mapCoordinates) {
        TargetedParticipantSummary summary = this.simpleNpcMovementService.findNpcByMapCoordinates(mapCoordinates);
        if (summary != null) {
            return summary;
        }

        summary = this.routeNpcMovementService.findNpcByMapCoordinates(mapCoordinates);
        if (summary != null) {
            return summary;
        }

        return this.customRouteNpcMovementService.findNpcByMapCoordinates(mapCoordinates);
    }

    /**
     * Sets the pause computation process of NPC.
     *
     * @param isPause - indicates when calculation should be paused.
     */
    public void setPause(boolean isPause) {
        this.simpleNpcMovementService.setPause(isPause);
        this.routeNpcMovementService.setPause(isPause);
        this.customRouteNpcMovementService.setPause(isPause);
    }

    /**
     * Sets the pause on participantComputator process for specific NPC.
     *
     * @param isPause   - indicates when calculation should be paused.
     * @param uuid      - the uuid of NPC to be paused
     */
    public void setPause(boolean isPause, String uuid) {
        this.simpleNpcMovementService.setPause(isPause, uuid);
        this.routeNpcMovementService.setPause(isPause, uuid);
        this.customRouteNpcMovementService.setPause(isPause, uuid);
    }

    /**
     * Tries to run movement for NPC participant.
     *
     * @param npcTargetUuid     - the NPC uuid.
     * @param x                 - the X coordinate to be moved.
     * @param y                 - the Y coordinate to be moved.
     * @param movementSpeed     - the speed of movement.
     * @param finalDirection    - the final direction of participant after finishing the movement.
     */
    public void tryToRunNpcMovement(String npcTargetUuid, int x, int y, int movementSpeed, MovementDirection finalDirection) {
        ActorParticipant actorParticipant = this.findParticipantFromService(npcTargetUuid);
        if (actorParticipant == null) {
            log.debug("NPC with given UUID {} not found", npcTargetUuid);
            return;
        }

        this.simpleNpcMovementService.removeEvaluableModel(npcTargetUuid);
        this.routeNpcMovementService.removeEvaluableModel(npcTargetUuid);

        RouteMovementEvaluableModel evaluableModel = new RouteMovementEvaluableModel(
                actorParticipant,
                MovementFactory.createGlobalCalculator(movementSpeed),
                MovementFactory.createRouteNpcStrategy(
                        false, Collections.singletonList(new RouteMovementDescription(x, y, finalDirection))
                )
        );
        evaluableModel.setOnComplete(this::onCustomRouteCompleted);

        this.customRouteNpcMovementService.addEvaluableModel(evaluableModel);
    }

    private Void onCustomRouteCompleted(ActorParticipant participant) {
        if (participant == null) {
            log.warn("Custom route complete handler invoked but doesn't get the participant.");
            return null;
        }

        this.customRouteNpcMovementService.removeEvaluableModel(participant.getUuid());
        this.addParticipantForComputation((NpcParticipant) participant);
        return null;
    }

    private ActorParticipant findParticipantFromService(String uuid) {
        if (this.simpleNpcMovementService.hasEvaluableModel(uuid)) {
            return this.simpleNpcMovementService.getEvaluableModel(uuid).getParticipant();
        } else if (this.routeNpcMovementService.hasEvaluableModel(uuid)) {
            return this.routeNpcMovementService.getEvaluableModel(uuid).getParticipant();
        }
        return null;
    }
}
