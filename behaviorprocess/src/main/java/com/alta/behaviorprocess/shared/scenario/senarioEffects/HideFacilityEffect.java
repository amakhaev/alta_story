package com.alta.behaviorprocess.shared.scenario.senarioEffects;

import com.alta.behaviorprocess.shared.data.EffectModel;
import com.alta.behaviorprocess.shared.data.HideFacilityEffectModel;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;
import lombok.Setter;

/**
 * Provides the interaction that handles the hide of facility.
 */
public class HideFacilityEffect implements Effect {

    private final EffectListener effectListener;

    @Setter
    private Runnable completeCallback;

    /**
     * Initialize new instance of {@link HideFacilityEffect}.
     *
     * @param effectListener - the listener of effects.
     */
    @AssistedInject
    public HideFacilityEffect(@Assisted @NonNull EffectListener effectListener) {
        this.effectListener = effectListener;
    }

    /**
     * Starts the interaction.
     *
     * @param effect - the effect to be shown.
     */
    @Override
    public void start(EffectModel effect) {
        if (effect.getType() != EffectModel.EffectType.HIDE_FACILITY) {
            throw new ClassCastException(
                    "The interaction effect has " + effect.getType() +
                            " type but required " + EffectModel.EffectType.HIDE_FACILITY
            );
        }
        this.effectListener.onHideFacility(((HideFacilityEffectModel) effect).getFacilityUuid());
        this.completeCallback.run();
    }

    /**
     * Triggers the next step in interaction.
     */
    @Override
    public void triggerNext() {
    }
}
