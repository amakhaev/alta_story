package com.alta.utils;

import java.util.concurrent.ThreadFactory;

/**
 * Initialize new instance of thread factory
 */
public class ThreadFactory implements java.util.concurrent.ThreadFactory {
    private int counter = 0;
    private final String prefix;

    /**
     * Initialize new instance of {@link java.util.concurrent.ThreadFactory}
     */
    public ThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, this.prefix + "-" + counter++);
    }
}
