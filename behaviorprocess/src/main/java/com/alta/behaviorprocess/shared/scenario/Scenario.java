package com.alta.behaviorprocess.shared.scenario;

/**
 * Provides the scenario that can be executed.
 */
public interface Scenario {

    /**
     * Executes of scenario.
     */
    void execute();

    /**
     * Run the next effect if needed.
     */
    void runNextEffect();

    /**
     * Indicates scenario completed or not.
     *
     * @return true if scenario has completed, false otherwise.
     */
    boolean isCompleted();
}
