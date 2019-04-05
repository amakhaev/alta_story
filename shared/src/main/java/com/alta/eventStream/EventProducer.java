package com.alta.eventStream;

import com.lmax.disruptor.EventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the event producer.
 */
public class EventProducer<T> extends EventStream<T> {

    private List<ProducerSubject<T>> subjects;

    /**
     * Initialize new instance of {@link EventProducer}
     */
    public EventProducer() {
        this.subjects = new ArrayList<>();
    }

    /**
     * The method that will be called before start of event streaming.
     */
    @Override
    protected void beforeStart() {
        if (this.subjects.size() == 0) {
            throw new RuntimeException("The subjects must be initialized before starting.");
        }


        this.subjects.forEach(s -> this.setHandleEvents(
                (EventHandler<GenericEvent<T>>) (event, sequence, endOfBatch) -> s.onHandle(event.getData()))
        );
    }

    /**
     * Subscribes to producing of events.
     *
     * @param subject - the subject that will get the push event.
     */
    public void subscribe(ProducerSubject<T> subject) {
        if (this.isStarted) {
            throw new RuntimeException("The producer already started, can't subscribe to it.");
        }

        this.subjects.add(subject);
    }
}
