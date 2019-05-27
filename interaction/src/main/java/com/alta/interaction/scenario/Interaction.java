package com.alta.interaction.scenario;

import com.alta.interaction.data.EffectModel;

/**
 * Provides the common interface to makes interaction.
 */
public interface Interaction {

    /**
     * Starts the interaction.
     *
     * @param effect - the effect to be performed.
     */
    void start(EffectModel effect);

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
