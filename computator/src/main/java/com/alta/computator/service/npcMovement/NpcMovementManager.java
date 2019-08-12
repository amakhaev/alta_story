package com.alta.computator.service.npcMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.actor.RouteNpcParticipant;
import com.alta.computator.service.computator.Computator;
import com.alta.computator.service.computator.ComputatorArgs;
import com.alta.computator.service.computator.ComputatorEvaluableModel;
import com.alta.computator.service.computator.movement.MovementFactory;
import com.alta.computator.service.computator.randomMovement.RandomMovementArgs;
import com.alta.computator.service.computator.randomMovement.RandomMovementComputator;
import com.alta.computator.service.computator.randomMovement.RandomMovementEvaluableModel;
import com.alta.computator.service.computator.routeMovement.RouteMovementArgs;
import com.alta.computator.service.computator.routeMovement.RouteMovementComputator;
import com.alta.computator.service.computator.routeMovement.RouteMovementEvaluableModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.stream.Stream;

/**
 * Provides the manager make computations related to NPC.
 */
@Slf4j
public class NpcMovementManager {

    private final NpcParticipantContainer<RandomMovementEvaluableModel> simpleNpcParticipantContainer;
    private final RandomMovementArgs randomMovementArgs;
    private final RandomMovementComputator randomMovementComputator;

    private final NpcParticipantContainer<RouteMovementEvaluableModel> routeNpcParticipantContainer;
    private final RouteMovementArgs routeMovementArgs;
    private final RouteMovementComputator routeMovementComputator;

    /**
     * Initialize new instance of {@link NpcMovementManager}.
     */
    public NpcMovementManager() {
        this.simpleNpcParticipantContainer = new NpcParticipantContainer<>();
        this.randomMovementArgs = new RandomMovementArgs();
        this.randomMovementComputator = new RandomMovementComputator();

        this.routeNpcParticipantContainer = new NpcParticipantContainer<>();
        this.routeMovementArgs = new RouteMovementArgs();
        this.routeMovementComputator = new RouteMovementComputator();
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
            this.simpleNpcParticipantContainer.addParticipant(
                    npcParticipant.getUuid(),
                    new RandomMovementEvaluableModel(
                            npcParticipant,
                            MovementFactory.createWorker(),
                            MovementFactory.createSimpleNpcStrategy(npcParticipant.getMovementType())
                    )
            );
        } else if (npcParticipant.getParticipantType() == ParticipatType.ROUTE_NPC) {
            RouteNpcParticipant routeNpcParticipant = (RouteNpcParticipant) npcParticipant;
            this.routeNpcParticipantContainer.addParticipant(
                    npcParticipant.getUuid(),
                    new RouteMovementEvaluableModel(
                            npcParticipant,
                            MovementFactory.createWorker(),
                            MovementFactory.createRouteNpcStrategy(
                                    routeNpcParticipant.isRouteLooped(), routeNpcParticipant.getRouteDescription()
                            )
                    )
            );
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
        this.randomMovementArgs.setAltitudeMap(altitudeMap);
        this.randomMovementArgs.setFocusPointGlobalCoordinates(focusPointGlobalCoordinates);
        this.randomMovementArgs.setDelta(delta);
        this.runComputation(this.simpleNpcParticipantContainer, this.randomMovementComputator, this.randomMovementArgs);

        this.routeMovementArgs.setAltitudeMap(altitudeMap);
        this.routeMovementArgs.setFocusPointGlobalCoordinates(focusPointGlobalCoordinates);
        this.routeMovementArgs.setDelta(delta);
        this.runComputation(this.routeNpcParticipantContainer, this.routeMovementComputator, this.routeMovementArgs);
    }

    /**
     * Gets the participant by given UUID.
     *
     * @param uuid - the UUID of participant to be found.
     * @return the {@link ActorParticipant} instance.
     */
    public ActorParticipant getParticipant(String uuid) {
        if (this.simpleNpcParticipantContainer.hasParticipant(uuid)) {
            return this.simpleNpcParticipantContainer.getParticipant(uuid).getParticipant();
        } else if (this.routeNpcParticipantContainer.hasParticipant(uuid)) {
            return this.routeNpcParticipantContainer.getParticipant(uuid).getParticipant();
        }

        return null;
    }

    /**
     * Finds the NPC that has given map coordinates.
     *
     * @param mapCoordinates - the map coordinates for searching.
     * @return the {@link TargetedParticipantSummary} instance of null if not found.
     */
    public TargetedParticipantSummary findNpcTargetByMapCoordinates(@NonNull Point mapCoordinates) {
        TargetedParticipantSummary result = this.findSimpleNpcTargetByMapCoordinates(mapCoordinates);

        if (result != null) {
            return result;
        }

        return this.findRouteNpcTargetByMapCoordinates(mapCoordinates);
    }

    /**
     * Sets the pause computation process of NPC.
     *
     * @param isPause - indicates when calculation should be paused.
     */
    public void setPause(boolean isPause) {
        Stream.concat(
                this.simpleNpcParticipantContainer.getInitializedParticipants().stream(),
                this.simpleNpcParticipantContainer.getInitializedParticipants().stream()
        ).forEach(evaluableModel -> evaluableModel.setComputationPause(isPause));

        Stream.concat(
                this.routeNpcParticipantContainer.getInitializedParticipants().stream(),
                this.routeNpcParticipantContainer.getInitializedParticipants().stream()
        ).forEach(evaluableModel -> evaluableModel.setComputationPause(isPause));
    }

    /**
     * Sets the pause on participantComputator process for specific NPC.
     *
     * @param isPause   - indicates when calculation should be paused.
     * @param uuid      - the uuid of NPC to be paused
     */
    public void setPause(boolean isPause, String uuid) {
        if (this.simpleNpcParticipantContainer.hasParticipant(uuid)) {
            this.simpleNpcParticipantContainer.getParticipant(uuid).setComputationPause(isPause);
        } else if (this.routeNpcParticipantContainer.hasParticipant(uuid)) {
            this.routeNpcParticipantContainer.getParticipant(uuid).setComputationPause(isPause);
        }
    }

    private <Evaluable extends ComputatorEvaluableModel, Args extends ComputatorArgs> void runComputation(
            NpcParticipantContainer<Evaluable> container, Computator<Evaluable, Args> computator, Args computatorArgs) {
        if (container.hasNotInitializedParticipants()) {
            container.getNotInitializedParticipants().forEach(
                    notInitializedParticipant -> computator.initialize(notInitializedParticipant, computatorArgs)
            );
            container.markAllAsInitialized();
        }

        if (container.hasInitializedParticipants()) {
            container.getInitializedParticipants().forEach(
                    evaluableParticipant -> computator.compute(evaluableParticipant, computatorArgs)
            );
        }
    }

    private TargetedParticipantSummary findSimpleNpcTargetByMapCoordinates(@NonNull Point mapCoordinates) {
        return Stream.concat(
                this.simpleNpcParticipantContainer.getInitializedParticipants().stream(),
                this.simpleNpcParticipantContainer.getInitializedParticipants().stream()
        )
                .filter(evaluableModel -> evaluableModel.getParticipant().getCurrentMapCoordinates().equals(mapCoordinates))
                .findFirst()
                .map(evaluableModel -> new TargetedParticipantSummary(
                        evaluableModel.getParticipant().getUuid(),
                        evaluableModel.getParticipant().getCurrentMapCoordinates(),
                        evaluableModel.getParticipant().getParticipantType()
                ))
                .orElse(null);
    }

    private TargetedParticipantSummary findRouteNpcTargetByMapCoordinates(@NonNull Point mapCoordinates) {
        return Stream.concat(
                this.routeNpcParticipantContainer.getInitializedParticipants().stream(),
                this.routeNpcParticipantContainer.getInitializedParticipants().stream()
        )
                .filter(evaluableModel -> evaluableModel.getParticipant().getCurrentMapCoordinates().equals(mapCoordinates))
                .findFirst()
                .map(evaluableModel -> new TargetedParticipantSummary(
                        evaluableModel.getParticipant().getUuid(),
                        evaluableModel.getParticipant().getCurrentMapCoordinates(),
                        evaluableModel.getParticipant().getParticipantType()
                ))
                .orElse(null);
    }
}
