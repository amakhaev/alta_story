package com.alta.computator.facade.updater;

/**
 * Provides the list of high level methods for updating current state.
 */
public interface UpdaterFacade {

    /**
     * Indicates when action captured internal data and currently working.
     *
     * @return true when action is working, false otherwise.
     */
    boolean isLock();

    /**
     * Handles the update of one state.
     *
     * @param delta - the delta between last and current calls.
     */
    void onUpdate(int delta);

}
