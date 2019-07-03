package com.alta.caching;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the implementation of lazy caching pattern for list.
 */
public class LazyCacheList<T> implements CacheList<T> {

    private List<T> values;

    /**
     * Initialize new instance of {@link LazyCacheList}.
     */
    public LazyCacheList() {
        this.values = new ArrayList<>();
    }

    /**
     * Pushes the values into cache.
     *
     * @param values - the values to be cached.
     */
    @Override
    public void pushAll(List<T> values) {
        this.values.addAll(values);
    }

    /**
     * Evicts values from cache before pushing the values into cache.
     *
     * @param values - the values to be cached.
     */
    @Override
    public void evictAndPushAll(List<T> values) {
        this.clear();
        this.pushAll(values);
    }

    /**
     * Gets the values from cache or null if cache is empty.
     *
     * @return the T instance.
     */
    @Override
    public List<T> get() {
        return this.values;
    }

    /**
     * Clears the cache.
     */
    @Override
    public void clear() {
        this.values.clear();
    }
}
