package com.alta.engine.asyncTask;

import com.alta.utils.ExecutorServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Provides the manager that do balancing of async tasks
 */
@Singleton
public class AsyncTaskManager {

    private static final String THREAD_POOL_NAME = "engine-main-thread";
    private static final byte THREAD_COUNT = 10;

    private static final String SCHEDULED_THREAD_POOL_NAME = "scheduled-engine-main-thread";
    private static final byte SCHEDULED_THREAD_COUNT = 1;

    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Initialize new instance of {@link AsyncTaskManager}
     */
    @Inject
    public AsyncTaskManager() {
        this.executorService = ExecutorServiceFactory.create(THREAD_COUNT, THREAD_POOL_NAME);
        this.scheduledExecutorService = ExecutorServiceFactory.createScheduled(SCHEDULED_THREAD_COUNT, SCHEDULED_THREAD_POOL_NAME);
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
     * @param intervalMillisecond - the interval between calls
     */
    public void runScheduledTask(Runnable task, long intervalMillisecond) {
        this.scheduledExecutorService.scheduleAtFixedRate(task, 0, intervalMillisecond, TimeUnit.MILLISECONDS);
    }
}
