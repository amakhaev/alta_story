package com.alta.engine.core.eventProducer.eventPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the data related to jumping of acting character.
 */
@Getter
@AllArgsConstructor
public class JumpingEventPayload {

    private final String mapName;
    private final Point mapStartCoordinates;

}
