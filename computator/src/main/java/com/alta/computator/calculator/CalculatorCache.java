package com.alta.computator.calculator;

import com.alta.caching.Cache;
import com.alta.caching.LazyCache;
import com.alta.computator.calculator.focusPoint.FocusPointMetadata;
import com.google.inject.Inject;

/**
 * Provides the cache for calculator.
 */
public class CalculatorCache {

    private Cache<FocusPointMetadata> focusPointMetadataCache;

    /**
     * Initialize new instance of {@link CalculatorCache}.
     */
    @Inject
    public CalculatorCache() {
        this.focusPointMetadataCache = new LazyCache<>();
    }

    /**
     * Pushes the metadata of focus point into calculator cache.
     *
     * @param focusPointMetadata - the object to be pushed.
     */
    public void pushFocusPointMetadata(FocusPointMetadata focusPointMetadata) {
        this.focusPointMetadataCache.push(focusPointMetadata);
    }

    /**
     * Gets the focus point metadata.
     *
     * @return the {@link FocusPointMetadata} instance.
     */
    public FocusPointMetadata getFocusPointMetadata() {
        return this.focusPointMetadataCache.get();
    }

    /**
     * Clears the calculator cache.
     */
    public void clear() {
        this.focusPointMetadataCache.clear();
    }
}
