package com.alta.computator.calculator.npc;

import com.alta.computator.calculator.MovementUpdater;
import com.alta.computator.core.computator.movement.MovementFactory;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.core.computator.movement.directionCalculation.RouteMovementDescription;
import com.alta.computator.core.computator.randomMovement.RandomMovementEvaluableModel;
import com.alta.computator.core.computator.routeMovement.RouteMovementEvaluableModel;
import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.core.storage.StorageReader;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Collections;

/**
 * Provides the implementation of movement calculator that applied to NPC only.
 */
@Slf4j
public class NpcMediatorImpl implements NpcMediator, MovementUpdater {

    private final StorageReader storageReader;
    private final NpcMovementService<RandomMovementEvaluableModel> simpleNpcMovementService;
    private final NpcMovementService<RouteMovementEvaluableModel> routeNpcMovementService;
    private final NpcMovementService<RouteMovementEvaluableModel> customRouteNpcMovementService;

    /**
     * Initialize new instance of {@link NpcMediatorImpl}.
     *
     * @param storageReader - the {@link StorageReader} instance.
     */
    public NpcMediatorImpl(StorageReader storageReader) {
        this.storageReader = storageReader;
        this.simpleNpcMovementService = new SimpleNpcMovementService();
        this.routeNpcMovementService = new RouteNpcMovementService();
        this.customRouteNpcMovementService = new RouteNpcMovementService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(int delta) {
        this.storageReader.getNpcParticipants().forEach(npcParticipant -> {
            if (this.getParticipant(npcParticipant.getUuid()) == null) {
                this.addParticipantForComputation(npcParticipant);
            }
        });

        this.simpleNpcMovementService.onCompute(
                this.storageReader.getAltitudeMap(), this.storageReader.getFocusPoint().getCurrentGlobalCoordinates(), delta
        );
        this.routeNpcMovementService.onCompute(
                this.storageReader.getAltitudeMap(), this.storageReader.getFocusPoint().getCurrentGlobalCoordinates(), delta
        );
        this.customRouteNpcMovementService.onComputeImmediately(
                this.storageReader.getAltitudeMap(), this.storageReader.getFocusPoint().getCurrentGlobalCoordinates(), delta
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public TargetedParticipantSummary findNpcTargetByMapCoordinates(Point mapCoordinates) {
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
     * {@inheritDoc}
     */
    @Override
    public void setPause(boolean isPause) {
        this.simpleNpcMovementService.setPause(isPause);
        this.routeNpcMovementService.setPause(isPause);
        this.customRouteNpcMovementService.setPause(isPause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPause(boolean isPause, String uuid) {
        this.simpleNpcMovementService.setPause(isPause, uuid);
        this.routeNpcMovementService.setPause(isPause, uuid);
        this.customRouteNpcMovementService.setPause(isPause, uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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

    private void addParticipantForComputation(NpcParticipant npcParticipant) {
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
