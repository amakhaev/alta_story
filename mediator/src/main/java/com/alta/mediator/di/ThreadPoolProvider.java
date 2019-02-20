package com.alta.mediator.di;

import com.alta.utils.ThreadPoolExecutor;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ThreadPoolProvider implements Provider<ThreadPoolExecutor> {

    private static final int POOL_SIZE = 5;
    private static final String POOL_NAME = "mediator_pool";

    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * Initialize new instance of {@link ThreadPoolProvider}
     */
    @Inject
    private ThreadPoolProvider() {
        this.threadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, POOL_NAME);
    }

    /**
     * Provides an instance of {@code T}.
     */
    @Override
    public ThreadPoolExecutor get() {
        return this.threadPoolExecutor;
    }
}
