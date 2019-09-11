package com.alta.computator.facade.updater;

import com.alta.computator.calculator.MovementUpdater;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Provides the implementation of computation updater.
 */
@Slf4j
public class UpdaterFacadeImpl implements UpdaterFacade {

    private final ReadWriteLock readWriteLock;
    private final List<MovementUpdater> updaters;

    /**
     * {@inheritDoc}
     */
    @Getter
    private boolean isLock;

    /**
     * Initialize new instance of {@link UpdaterFacadeImpl}.
     */
    public UpdaterFacadeImpl(MovementUpdater focusPointUpdater,
                             MovementUpdater mapUpdater,
                             MovementUpdater actingCharacterUpdater,
                             MovementUpdater facilityUpdater,
                             MovementUpdater npcUpdater) {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.updaters = Arrays.asList(
                focusPointUpdater, mapUpdater, facilityUpdater, actingCharacterUpdater, npcUpdater
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(int delta) {
        if (this.isLock()) {
            log.warn("Computator updater already working. No actions will perform.");
            return;
        }

        try {
            this.readWriteLock.writeLock().lock();
            this.isLock = true;
            this.updaters.forEach(updater -> updater.onUpdate(delta));
        } finally {
            this.isLock = false;
            this.readWriteLock.writeLock().unlock();
        }
    }

}
