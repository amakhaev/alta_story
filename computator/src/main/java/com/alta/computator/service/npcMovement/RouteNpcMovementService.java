package com.alta.computator.service.npcMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.actor.RouteNpcParticipant;
import com.alta.computator.service.computator.movement.MovementFactory;
import com.alta.computator.service.computator.routeMovement.RouteMovementArgs;
import com.alta.computator.service.computator.routeMovement.RouteMovementComputator;
import com.alta.computator.service.computator.routeMovement.RouteMovementEvaluableModel;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.stream.Stream;

/**
 * Provides the implementation of movement service for simple NPC.
 */
@Slf4j
class RouteNpcMovementService implements NpcMovementService<RouteMovementEvaluableModel> {

    private final NpcParticipantContainer<RouteMovementEvaluableModel> routeNpcParticipantContainer;
    private final RouteMovementArgs routeMovementArgs;
    private final RouteMovementComputator routeMovementComputator;

    /**
     * Initialize new instance of {@link RouteNpcMovementService}.
     */
    public RouteNpcMovementService() {
        this.routeNpcParticipantContainer = new NpcParticipantContainer<>();
        this.routeMovementArgs = new RouteMovementArgs();
        this.routeMovementComputator = new RouteMovementComputator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParticipant(NpcParticipant npcParticipant) {
        if (npcParticipant.getParticipantType() != ParticipatType.ROUTE_NPC) {
            log.warn(
                    "Invalid type of participant type, expected: {}, actual: {}",
                    ParticipatType.ROUTE_NPC,
                    npcParticipant.getParticipantType()
            );
            return;
        }

        RouteNpcParticipant routeNpcParticipant = (RouteNpcParticipant) npcParticipant;
        this.addEvaluableModel(new RouteMovementEvaluableModel(
                npcParticipant,
                MovementFactory.createGlobalCalculator(routeNpcParticipant.getMovementSpeed()),
                MovementFactory.createRouteNpcStrategy(
                        routeNpcParticipant.isRouteLooped(), routeNpcParticipant.getRouteDescription()
                )
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEvaluableModel(RouteMovementEvaluableModel evaluableModel) {
        if (evaluableModel == null) {
            log.error("Adding of participant was failed since evaluable model is null");
        } else if (evaluableModel.getParticipant() == null) {
            log.error("Adding of participant was failed since participant is null");
        } else if (evaluableModel.getGlobalMovementCalculator() == null) {
            log.error("Adding of participant was failed since global calculator is null");
        } else if (evaluableModel.getMovementDirectionStrategy() == null) {
            log.error("Adding of participant was failed since direction strategy is null");
        } else {
            this.routeNpcParticipantContainer.addParticipant(evaluableModel.getParticipant().getUuid(),evaluableModel);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEvaluableModel(String uuid) {
        this.routeNpcParticipantContainer.removeParticipant(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        this.compute(altitudeMap, focusPointGlobalCoordinates, delta, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onComputeImmediately(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        this.compute(altitudeMap, focusPointGlobalCoordinates, delta, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasEvaluableModel(String uuid) {
        return this.routeNpcParticipantContainer.hasParticipant(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RouteMovementEvaluableModel getEvaluableModel(String uuid) {
        return this.hasEvaluableModel(uuid) ? this.routeNpcParticipantContainer.getParticipant(uuid) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TargetedParticipantSummary findNpcByMapCoordinates(Point mapCoordinates) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPause(boolean isPause) {
        Stream.concat(
                this.routeNpcParticipantContainer.getInitializedParticipants().stream(),
                this.routeNpcParticipantContainer.getInitializedParticipants().stream()
        ).forEach(evaluableModel -> evaluableModel.setComputationPause(isPause));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPause(boolean isPause, String uuid) {
        if (this.routeNpcParticipantContainer.hasParticipant(uuid)) {
            this.routeNpcParticipantContainer.getParticipant(uuid).setComputationPause(isPause);
        }
    }
    
    private void compute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta, boolean isRunImmediately) {
        this.routeMovementArgs.setAltitudeMap(altitudeMap);
        this.routeMovementArgs.setFocusPointGlobalCoordinates(focusPointGlobalCoordinates);
        this.routeMovementArgs.setDelta(delta);
        this.routeMovementArgs.setRunImmediately(isRunImmediately);

        if (this.routeNpcParticipantContainer.hasNotInitializedParticipants()) {
            this.routeNpcParticipantContainer.getNotInitializedParticipants().forEach(
                    notInitializedParticipant -> this.routeMovementComputator.initialize(
                            notInitializedParticipant, this.routeMovementArgs
                    )
            );
            this.routeNpcParticipantContainer.markAllAsInitialized();
        }

        if (this.routeNpcParticipantContainer.hasInitializedParticipants()) {
            this.routeNpcParticipantContainer.getInitializedParticipants().forEach(
                    evaluableParticipant -> this.routeMovementComputator.compute(
                            evaluableParticipant, this.routeMovementArgs
                    )
            );
        }
    }
}
