package com.alta.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides the async task executor for multithreading working
 */
@Slf4j
public class ThreadPoolExecutor {

    private ExecutorService executorService;

    /**
     * Initialize new instance of {@link ThreadPoolExecutor}
     */
    public ThreadPoolExecutor(int threadCount, String poolName) {
        this.executorService = Executors.newFixedThreadPool(threadCount,new NamedThreadFactory(poolName));
    }

    /**
     * Ran the action in new thread
     *
     * @param runnable - the action that should be performed
     * @param taskName - the name of task for execution
     */
    public void run(String taskName, Runnable runnable) {
        log.debug("Start execution of '{}' task", taskName);
        this.executorService.execute(runnable);
    }
}
