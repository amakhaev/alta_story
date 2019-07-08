package com.alta.caching;

import java.util.List;

/**
 * Provides the cache for list of any model.
 */
public interface CacheList<T> {

    /**
     * Pushes the values into cache.
     *
     * @param values - the values to be cached.
     */
    void pushAll(List<T> values);

    /**
     * Evicts values from cache before pushing the values into cache.
     *
     * @param values - the values to be cached.
     */
    void evictAndPushAll(List<T> values);

    /**
     * Gets the values from cache or null if cache is empty.
     *
     * @return the T instance.
     */
    List<T> get();

    /**
     * Clears the cache.
     */
    void clear();

}
