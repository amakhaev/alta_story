package com.alta.mediator.di;

import com.alta.utils.ExecutorServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.concurrent.ExecutorService;

public class ThreadPoolProvider implements Provider<ExecutorService> {

    private static final int POOL_SIZE = 5;
    private static final String POOL_NAME = "mediator_pool";

    private ExecutorService executorService;

    /**
     * Initialize new instance of {@link ThreadPoolProvider}
     */
    @Inject
    private ThreadPoolProvider() {
        this.executorService = ExecutorServiceFactory.create(POOL_SIZE, POOL_NAME);
    }

    /**
     * Provides an instance of {@code T}.
     */
    @Override
    public ExecutorService get() {
        return this.executorService;
    }
}
