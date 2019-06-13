package com.alta.eventStream;

/**
 * Provides the subject that emit the {@link EventProducer}
 */
@FunctionalInterface
public interface ProducerSubject<T> {

    /**
     * Handles the event.
     *
     * @param data - the data that received from event producer
     */
    void onHandle(T data);

}
