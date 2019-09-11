package com.alta.caching;

/**
 * Provides the implementation of lazy caching pattern.
 */
public class LazyCache<T> implements Cache<T> {

    private T value;

    /**
     * Initialize new instance of {@link LazyCache}.
     */
    public LazyCache() {
    }

    /**
     * Pushes the values into cache.
     *
     * @param value - the values to be cached.
     */
    @Override
    public synchronized void push(T value) {
        this.value = value;
    }

    /**
     * Gets the values from cache or null if cache is empty.
     *
     * @return the T instance.
     */
    @Override
    public T get() {
        return this.value;
    }

    /**
     * Cleans the cache.
     */
    @Override
    public void clear() {
        this.value = null;
    }
}
