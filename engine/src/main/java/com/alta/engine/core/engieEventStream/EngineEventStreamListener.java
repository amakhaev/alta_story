package com.alta.engine.core.engieEventStream;

/**
 * Provides the listener of {@link EngineEventStream}
 */
@FunctionalInterface
public interface EngineEventStreamListener<T> {

    /**
     * Handles the getting of stream data
     *
     * @param data - the data that received from event stream
     */
    void handle(T data);

}
