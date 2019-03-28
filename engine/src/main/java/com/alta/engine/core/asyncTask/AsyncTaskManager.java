package com.alta.engine.core.asyncTask;

import com.alta.utils.ExecutorServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Provides the manager that do balancing of async tasks
 */
@Slf4j
@Singleton
public class AsyncTaskManager {

    private static final String THREAD_POOL_NAME = "engine-main-thread";
    private static final byte THREAD_COUNT = 10;

    private static final String SCHEDULED_THREAD_POOL_NAME = "scheduled-engine-main-thread";
    private static final byte SCHEDULED_THREAD_COUNT = 2;

    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Map<String, ScheduledFuture<?>> scheduledFutures;

    /**
     * Initialize new instance of {@link AsyncTaskManager}
     */
    @Inject
    public AsyncTaskManager() {
        this.executorService = ExecutorServiceFactory.create(THREAD_COUNT, THREAD_POOL_NAME);
        this.scheduledExecutorService = ExecutorServiceFactory.createScheduled(SCHEDULED_THREAD_COUNT, SCHEDULED_THREAD_POOL_NAME);
        this.scheduledFutures = new HashMap<>();
    }

    /**
     * Executes the task with given name
     *
     * @param name - the name of task
     * @param task - the task that should be executed
     */
    public void executeTask(String name, Runnable task) {
        this.executorService.execute(() -> {
            Thread.currentThread().setName(Thread.currentThread().getName() + "_" + name);
            task.run();
        });
    }

    /**
     * Run the task in the loop by given interval
     *
     * @param task - the task to be executed each time
     * @param name - the name of task.
     * @param intervalMillisecond - the interval between calls
     */
    public void runScheduledTask(Runnable task, String name, long intervalMillisecond) {
        ScheduledFuture<?> future = this.scheduledExecutorService.scheduleAtFixedRate(
                task, 0, intervalMillisecond, TimeUnit.MILLISECONDS
        );

        if (this.scheduledFutures.containsKey(name)) {
            this.stopScheduledTask(name);
        }

        this.scheduledFutures.put(name, future);
    }

    /**
     * Stops the execution of scheduled task
     *
     * @param name - the name of task.
     */
    public void stopScheduledTask(String name) {
        if (!this.scheduledFutures.containsKey(name)) {
            return;
        }

        try {
            this.scheduledFutures.get(name).cancel(true);
        } catch (Exception e) {
            log.error("Can't stop scheduled task because. {}", e.getMessage());
        }
    }
}
