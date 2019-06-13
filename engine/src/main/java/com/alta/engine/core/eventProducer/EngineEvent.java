package com.alta.engine.core.eventProducer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the engine event.
 */
@Slf4j
@Getter
@AllArgsConstructor
public class EngineEvent {

    private final EngineEventType type;
    private final Object payload;

    /**
     * Tries to case the payload to specific type.
     *
     * @param resultType - the type that wanted to get.
     * @return the payload casted to specific type.
     */
    public <T> T tryToCastPayload(Class<T> resultType) {
        if (this.payload == null) {
            return null;
        }

        try {
            return resultType.cast(this.payload);
        } catch (ClassCastException e) {
            log.info(
                    "Can't cast payload payloadType: {}, resultType: {}. Error: {}",
                    this.payload.getClass().getSimpleName(),
                    resultType.getSimpleName(),
                    e.getMessage()
            );
            return null;
        }
    }
}
