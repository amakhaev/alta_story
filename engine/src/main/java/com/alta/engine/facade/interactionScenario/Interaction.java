package com.alta.engine.facade.interactionScenario;

import com.alta.engine.model.interaction.InteractionEffectEngineModel;

/**
 * Provides the common interface to makes interaction.
 */
public interface Interaction {

    /**
     * Starts the interaction.
     *
     * @param effect - the effect to be shown.
     */
    void start(InteractionEffectEngineModel effect);

    /**
     * Triggers the next step in interaction.
     */
    void triggerNext();

    /**
     * Sets the callback that will be triggered when interaction will be finished.
     *
     * @param callback - the callback to be triggered.
     */
    void setCompleteCallback(Runnable callback);

}
