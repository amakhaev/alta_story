package com.alta.computator.calculator;

/**
 * Provides the list of high level methods to make movement calculations.
 */
public interface MovementUpdater {

    /**
     * Handles the update of one state.
     *
     * @param delta - the delta between last and current calls.
     */
    void onUpdate(int delta);

}
