package com.alta.eventStream;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * Provides the event stream class that included logic by creating message service and publishing events
 */
abstract class EventStream<T> {

    private static final int BUFFER_SIZE = 1024;

    private final Disruptor<GenericEvent<T>> disruptor;
    private final RingBuffer<GenericEvent<T>> ringBuffer;

    protected boolean isStarted;

    /**
     * Initialize ew instance of {@link EventStream}
     */
    EventStream() {
        this.disruptor = new Disruptor<>(
                new GenericEventFactory<>(),
                BUFFER_SIZE,
                DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );
        this.ringBuffer = this.disruptor.getRingBuffer();
        this.isStarted = false;
    }

    /**
     * Starts the streaming of events
     */
    public final void start() {
        this.beforeStart();
        this.disruptor.start();
        this.isStarted = true;
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
     * Sets the event interaction
     *
     * @param handlers - the event interaction that will be called when event published
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
