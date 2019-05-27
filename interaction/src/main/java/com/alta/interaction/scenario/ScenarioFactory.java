package com.alta.interaction.scenario;

import com.google.inject.assistedinject.Assisted;
import lombok.NonNull;

/**
 * Provides the factory related to interaction scenario.
 */
public interface ScenarioFactory {

    /**
     * Creates new instance of {@link Scenario}.
     *
     * @param successCallback - the callback to invoke when scenario completed successfully.
     * @param failCallback - the callback to invoke when scenario fail.
     * @return create {@link Scenario} instance.
     */
    Scenario createInteractionScenario(@Assisted @NonNull EffectListener effectListener,
                                       @Assisted("successCallback") Runnable successCallback,
                                       @Assisted("failCallback") Runnable failCallback);

}
