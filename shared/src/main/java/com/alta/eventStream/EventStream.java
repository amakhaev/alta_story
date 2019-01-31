package com.alta.eventStream;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * Provides the event stream class that included logic by creating message service and publishing events
 */
public final class EventStream<T> {

    private static final int BUFFER_SIZE = 1024;

    private final Disruptor<GenericEvent<T>> disruptor;
    private final RingBuffer<GenericEvent<T>> ringBuffer;

    /**
     * Initialize ew instance of {@link EventStream}
     */
    public EventStream() {
        this.disruptor = new Disruptor(new GenericEventFactory<T>(),
                BUFFER_SIZE,
                DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );
        this.ringBuffer = this.disruptor.getRingBuffer();
    }

    /**
     * Sets the event handlers
     *
     * @param handlers - the event handlers that will be called when event published
     * @return current {@link EventStream} instance
     */
    public final EventStream<T> setHandleEvents(final EventHandler<GenericEvent<T>>... handlers) {
        this.disruptor.handleEventsWith(handlers).then(new ClearingEventHandler<>());
        return this;
    }

    /**
     * Publishes the event
     *
     * @param eventData - the event data
     */
    public final void publishEvent(T eventData) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        this.ringBuffer.publishEvent((e, sequence, buffer) -> e.setData(eventData), bb);
    }

    public final void start() {
        this.disruptor.start();
    }
}
