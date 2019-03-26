package com.alta.engine.core.engieEventStream;

import com.alta.eventStream.EventStream;
import com.alta.eventStream.GenericEvent;
import com.lmax.disruptor.EventHandler;

/**
 * Provides the event stream related to engine
 */
public class EngineEventStream<T> extends EventStream<T> {

    private EngineEventStreamListener<T> listener;

    /**
     * Initialize new instance of {@link EngineEventStream}
     */
    public EngineEventStream(EngineEventStreamListener<T> listener) {
        super();
        this.setListener(listener);
    }

    /**
     * Initialize new instance of {@link EngineEventStream}
     */
    public EngineEventStream() {
        this(null);
    }

    /**
     * Sets the listener for event stream in the engine
     *
     * @param listener - the listener to be called.
     */
    public void setListener(EngineEventStreamListener<T> listener) {
        this.listener = listener;
    }

    /**
     * The method that will be called before start of event streaming.
     */
    @Override
    public void beforeStart() {
        this.setHandleEvents((EventHandler<GenericEvent<T>>) (event, sequence, endOfBatch) -> {
            if (this.listener != null) {
                this.listener.handle(event.getData());
            }
        });
    }
}
