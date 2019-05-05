package com.alta.computator.service.stage;

import com.alta.computator.model.event.ActingCharacterJumpEvent;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.event.ComputatorEventType;
import com.alta.computator.service.movement.actor.ActingCharacterEventListener;
import com.alta.eventStream.EventProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the listener for events with acting character.
 */
@Slf4j
@AllArgsConstructor
public class ActingCharacterEventListenerImpl implements ActingCharacterEventListener {

    private final EventProducer<ComputatorEvent> jumpEventProducer;

    /**
     * Handles the event that invoked when acting character targeted to jump point on map.
     *
     * @param targetPoint         - the point to which character was targeted.
     * @param actingCharacterUuid - the uuid of acting character.
     */
    @Override
    public void onAfterMovingToJumpPoint(Point targetPoint, String actingCharacterUuid) {
        log.info("The acting character {} moved to jump point {}", actingCharacterUuid, targetPoint);
        this.jumpEventProducer.publishEvent(new ActingCharacterJumpEvent(
                ComputatorEventType.ACTING_CHARACTER_JUMP, targetPoint
        ));
    }
}
