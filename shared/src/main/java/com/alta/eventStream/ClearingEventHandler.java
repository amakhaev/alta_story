package com.alta.eventStream;

import com.lmax.disruptor.EventHandler;

/**
 * The event handler that clears the event after handling
 */
class ClearingEventHandler<T> implements EventHandler<T> {

    @Override
    public void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
        event = null;
    }
}
