package com.alta.computator.service.stage;

import com.alta.computator.model.event.ActingCharacterJumpEvent;
import com.alta.computator.model.event.ComputatorEvent;
import com.alta.computator.model.event.ComputatorEventType;
import com.alta.computator.service.movement.focusPoint.FocusPointEventListener;
import com.alta.eventStream.EventProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the implementation of listener for focus point.
 */
@Slf4j
@AllArgsConstructor
public class FocusPointEventListenerImpl implements FocusPointEventListener {

    private final EventProducer<ComputatorEvent> jumpEventProducer;

    /**
     * Handles the event that invoked when focus point targeted to jump point on map.
     *
     * @param targetPoint - the point to which focus was targeted.
     */
    @Override
    public void onBeforeMovingToJumpTile(Point targetPoint) {
        log.info("The focus point will be moved to jump point {}", targetPoint);
        this.jumpEventProducer.publishEvent(new ActingCharacterJumpEvent(
                ComputatorEventType.BEFORE_MOVING_FOCUS_TO_JUMP_POINT, targetPoint
        ));
    }
}
