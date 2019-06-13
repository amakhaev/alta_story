package com.alta.engine.core.eventProducer.eventPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the payload contains data related to saving of current state.
 */
@Getter
@AllArgsConstructor
public class SaveStateEventPayload {

    private final String mapName;
    private final String skinName;
    private final Point mapCoordinates;

}
