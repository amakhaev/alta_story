package com.alta.computator.calculator.npc;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.core.computator.movement.MovementFactory;
import com.alta.computator.core.computator.randomMovement.RandomMovementArgs;
import com.alta.computator.core.computator.randomMovement.RandomMovementComputator;
import com.alta.computator.core.computator.randomMovement.RandomMovementEvaluableModel;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.stream.Stream;

/**
 * Provides the implementation of movement service for simple NPC.
 */
@Slf4j
public class SimpleNpcMovementService implements NpcMovementService<RandomMovementEvaluableModel> {

    private final NpcParticipantContainer<RandomMovementEvaluableModel> simpleNpcParticipantContainer;
    private final RandomMovementArgs randomMovementArgs;
    private final RandomMovementComputator randomMovementComputator;

    /**
     * Initialize ew instance of {@link SimpleNpcMovementService}.
     */
    public SimpleNpcMovementService() {
        this.simpleNpcParticipantContainer = new NpcParticipantContainer<>();
        this.randomMovementArgs = new RandomMovementArgs();
        this.randomMovementComputator = new RandomMovementComputator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParticipant(NpcParticipant npcParticipant) {
        this.addEvaluableModel(
                new RandomMovementEvaluableModel(
                        npcParticipant,
                        MovementFactory.createGlobalCalculator(),
                        MovementFactory.createSimpleNpcStrategy(npcParticipant.getMovementType())
                )
        );
    }

    @Override
    public void addEvaluableModel(RandomMovementEvaluableModel evaluableModel) {
        if (evaluableModel == null) {
            log.error("Adding of participant was failed since evaluable model is null");
        } else if (evaluableModel.getParticipant() == null) {
            log.error("Adding of participant was failed since participant is null");
        } else if (evaluableModel.getGlobalMovementCalculator() == null) {
            log.error("Adding of participant was failed since global action is null");
        } else if (evaluableModel.getMovementDirectionStrategy() == null) {
            log.error("Adding of participant was failed since direction strategy is null");
        } else {
            this.simpleNpcParticipantContainer.addParticipant(evaluableModel.getParticipant().getUuid(), evaluableModel);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEvaluableModel(String uuid) {
        this.simpleNpcParticipantContainer.removeParticipant(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        this.randomMovementArgs.setAltitudeMap(altitudeMap);
        this.randomMovementArgs.setFocusPointGlobalCoordinates(focusPointGlobalCoordinates);
        this.randomMovementArgs.setDelta(delta);

        if (this.simpleNpcParticipantContainer.hasNotInitializedParticipants()) {
            this.simpleNpcParticipantContainer.getNotInitializedParticipants().forEach(
                    notInitializedParticipant -> this.randomMovementComputator.initialize(
                            notInitializedParticipant, this.randomMovementArgs
                    )
            );
            this.simpleNpcParticipantContainer.markAllAsInitialized();
        }

        if (this.simpleNpcParticipantContainer.hasInitializedParticipants()) {
            this.simpleNpcParticipantContainer.getInitializedParticipants().forEach(
                    evaluableParticipant -> this.randomMovementComputator.compute(
                            evaluableParticipant, this.randomMovementArgs
                    )
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onComputeImmediately(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        this.onCompute(altitudeMap, focusPointGlobalCoordinates, delta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasEvaluableModel(String uuid) {
        return this.simpleNpcParticipantContainer.hasParticipant(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomMovementEvaluableModel getEvaluableModel(String uuid) {
        return this.hasEvaluableModel(uuid) ? this.simpleNpcParticipantContainer.getParticipant(uuid) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TargetedParticipantSummary findNpcByMapCoordinates(Point mapCoordinates) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPause(boolean isPause) {
        Stream.concat(
                this.simpleNpcParticipantContainer.getInitializedParticipants().stream(),
                this.simpleNpcParticipantContainer.getInitializedParticipants().stream()
        ).forEach(evaluableModel -> evaluableModel.setComputationPause(isPause));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPause(boolean isPause, String uuid) {
        if (this.simpleNpcParticipantContainer.hasParticipant(uuid)) {
            this.simpleNpcParticipantContainer.getParticipant(uuid).setComputationPause(isPause);
        }
    }
}
