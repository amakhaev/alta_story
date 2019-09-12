package com.alta.computator.facade.action;

import com.alta.computator.calculator.actingCharacter.ActingCharacterMediator;
import com.alta.computator.calculator.focusPoint.FocusPointMediator;
import com.alta.computator.calculator.npc.NpcMediator;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.calculator.actingCharacter.ActingCharacterEventListenerImpl;
import com.alta.computator.calculator.focusPoint.FocusPointEventListenerImpl;
import com.alta.eventStream.EventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.function.Function;

/**
 * Provides the list of high level method to make actions in the system.
 */
@Slf4j
@RequiredArgsConstructor
public class ActionFacadeImpl implements ActionFacade {

    private final ActingCharacterMediator actingCharacterMediator;
    private final FocusPointMediator focusPointMediator;
    private final NpcMediator npcMediator;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEventListener(EventProducer<ComputatorEvent> eventProducer) {
        this.actingCharacterMediator.setEventListener(new ActingCharacterEventListenerImpl(eventProducer));
        this.focusPointMediator.setEventListener(new FocusPointEventListenerImpl(eventProducer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tryToRunActingCharacterMovement(MovementDirection movementDirection) {
        this.focusPointMediator.tryToRunMovement(movementDirection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPauseForAll(boolean isPause) {
        this.focusPointMediator.setPause(isPause);
        this.npcMediator.setPause(isPause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNpcPause(boolean isPause, String uuid) {
        this.npcMediator.setPause(isPause, uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tryToRunNpcMovement(String npcTargetUuid,
                                    Point targetMapCoordinates,
                                    int movementSpeed,
                                    MovementDirection finalDirection,
                                    Function<String, Void> completeCallback) {
        this.npcMediator.tryToRunNpcMovement(
                npcTargetUuid, targetMapCoordinates.x, targetMapCoordinates.y, movementSpeed, finalDirection, completeCallback
        );
    }
}
