package com.alta.eventStream;

import com.lmax.disruptor.EventFactory;

/**
 * Provides the factory that creates the {@link GenericEvent}
 */
public class GenericEventFactory<T> implements EventFactory<GenericEvent<T>> {
    @Override
    public GenericEvent<T> newInstance() {
        return new GenericEvent<>();
    }
}
