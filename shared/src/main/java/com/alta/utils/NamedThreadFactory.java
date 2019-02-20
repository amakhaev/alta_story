package com.alta.utils;

import java.util.concurrent.ThreadFactory;

/**
 * Initialize new instance of thread factory
 */
public class NamedThreadFactory implements java.util.concurrent.ThreadFactory {
    private int counter = 0;
    private final String prefix;

    /**
     * Initialize new instance of {@link java.util.concurrent.ThreadFactory}
     */
    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, this.prefix + "-" + counter++);
    }
}
