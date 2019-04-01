package com.alta.engine.core.engineEventStream;

/**
 * Provides the listener of {@link EngineEventStream}
 */
@FunctionalInterface
public interface EngineEventStreamListener<T> {

    /**
     * Handles the getting of stream model
     *
     * @param data - the model that received from event stream
     */
    void handle(T data);

}
