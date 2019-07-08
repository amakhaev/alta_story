package com.alta.caching;

/**
 * Provides the cache for model.
 */
public interface Cache<T> {

    /**
     * Pushes the values into cache.
     *
     * @param value - the values to be cached.
     */
    void push(T value);

    /**
     * Gets the values from cache or null if cache is empty.
     *
     * @return the T instance.
     */
    T get();

    /**
     * Clears the cache.
     */
    void clear();

}
