package com.alta.engine.facade.interactionScenario;

/**
 * Provides the factory related to interaction scenario.
 */
public interface InteractionScenarioFactory {

    /**
     * Creates new instance of {@link InteractionScenario}.
     *
     * @param completeCallback - the callback to invoke when scenario completed.
     * @return create {@link InteractionScenario} instance.
     */
    InteractionScenario createInteractionScenario(Runnable completeCallback);

}
