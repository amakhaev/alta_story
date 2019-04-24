package com.alta.engine.facade.interactionScenario;

import com.google.inject.assistedinject.Assisted;

/**
 * Provides the factory related to interaction scenario.
 */
public interface InteractionScenarioFactory {

    /**
     * Creates new instance of {@link InteractionScenario}.
     *
     * @param successCallback - the callback to invoke when scenario completed successfully.
     * @param failCallback - the callback to invoke when scenario fail.
     * @return create {@link InteractionScenario} instance.
     */
    InteractionScenario createInteractionScenario(@Assisted("successCallback") Runnable successCallback,
                                                  @Assisted("failCallback") Runnable failCallback);

}
