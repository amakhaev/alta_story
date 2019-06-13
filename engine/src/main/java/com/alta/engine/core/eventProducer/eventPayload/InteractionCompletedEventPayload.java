package com.alta.engine.core.eventProducer.eventPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the payload related to completing of interaction.
 */
@Getter
@AllArgsConstructor
public class InteractionCompletedEventPayload {

    private final String interactionUuid;
    private final String mapName;

}
