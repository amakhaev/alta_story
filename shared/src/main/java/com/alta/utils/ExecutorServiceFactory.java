package com.alta.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Provides the async task executor for multithreading working
 */
@UtilityClass
public class ExecutorServiceFactory {

    /**
     * Creates the ExecutorService by given args
     *
     * @param threadCount - the count of threads that available in {@link ExecutorService}
     * @param poolName - the name of threads in ExecutorService
     * @return created {@link ExecutorService} instance
     */
    public ExecutorService create(int threadCount, String poolName) {
        return Executors.newFixedThreadPool(threadCount,new NamedThreadFactory(poolName));
    }

    /**
     * Creates the scheduled service with single thread
     *
     * @param threadCount - - the count of threads that available.
     * @param threadName - the name of thread
     * @return the {@link ScheduledExecutorService} instance
     */
    public ScheduledExecutorService createScheduled(int threadCount, String threadName) {
        return Executors.newScheduledThreadPool(threadCount, new NamedThreadFactory(threadName));
    }
}
