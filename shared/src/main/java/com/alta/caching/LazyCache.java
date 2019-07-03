package com.alta.caching;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Provides the implementation of lazy caching pattern.
 */
public class LazyCache<T> implements Cache<T> {

    private Deque<T> values;

    /**
     * Initialize new instance of {@link LazyCache}.
     */
    public LazyCache() {
        this.values = new LinkedList<>();
    }

    /**
     * Pushes the values into cache.
     *
     * @param value - the values to be cached.
     */
    @Override
    public void push(T value) {
        if (!this.values.isEmpty()) {
            this.values.removeFirst();
        }

        this.values.push(value);
    }

    /**
     * Gets the values from cache or null if cache is empty.
     *
     * @return the T instance.
     */
    @Override
    public T get() {
        return this.values.peekFirst();
    }

    /**
     * Cleans the cache.
     */
    @Override
    public void clear() {
        this.values.clear();
    }
}
