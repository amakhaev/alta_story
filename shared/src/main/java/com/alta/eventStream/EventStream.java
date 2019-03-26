package com.alta.eventStream;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides the event stream class that included logic by creating message service and publishing events
 */
public abstract class EventStream<T> {

    private static final int BUFFER_SIZE = 1024;

    private final Disruptor<GenericEvent<T>> disruptor;
    private final RingBuffer<GenericEvent<T>> ringBuffer;

    /**
     * Initialize ew instance of {@link EventStream}
     */
    public EventStream() {
        this.disruptor = new Disruptor<>(
                new GenericEventFactory<>(),
                BUFFER_SIZE,
                DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );
        this.ringBuffer = this.disruptor.getRingBuffer();
    }

    /**
     * Starts the streaming of events
     */
    public final void start() {
        this.beforeStart();
        this.disruptor.start();
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

    /**
     * Sets the event handlers
     *
     * @param handlers - the event handlers that will be called when event published
     * @return current {@link EventStream} instance
     */
    protected final EventStream<T> setHandleEvents(final EventHandler<GenericEvent<T>>... handlers) {
        this.disruptor.handleEventsWith(handlers).then(new ClearingEventHandler<>());
        return this;
    }

    /**
     * The method that will be called before start of event streaming.
     */
    protected void beforeStart() {
    }
}
