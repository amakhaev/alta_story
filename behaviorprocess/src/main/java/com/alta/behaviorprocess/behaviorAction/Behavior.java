package com.alta.behaviorprocess.behaviorAction;

import com.alta.behaviorprocess.shared.scenario.Scenario;

/**
 * Provides the common behavior interface.
 */
public interface Behavior<T> {

    /**
     * Gets the scenario by given params.
     *
     * @param scenarioParams - the params that applied to scenario.
     * @return created {@link Scenario} instance.
     */
    Scenario getScenario(T scenarioParams);

}
