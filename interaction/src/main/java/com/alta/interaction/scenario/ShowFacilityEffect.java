package com.alta.interaction.scenario;

import com.alta.interaction.data.EffectModel;
import com.alta.interaction.data.ShowFacilityEffectModel;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;
import lombok.Setter;

/**
 * Provides the interaction that handles the show of facility.
 */
public class ShowFacilityEffect implements Interaction {

    private final EffectListener effectListener;

    @Setter
    private Runnable completeCallback;

    /**
     * Initialize new instance of {@link ShowFacilityEffect}.
     */
    @AssistedInject
    public ShowFacilityEffect(@Assisted @NonNull EffectListener effectListener) {
        this.effectListener = effectListener;
    }

    /**
     * Starts the interaction.
     *
     * @param effect - the effect to be shown.
     */
    @Override
    public void start(EffectModel effect) {
        if (effect.getType() != EffectModel.EffectType.SHOW_FACILITY) {
            throw new ClassCastException(
                    "The interaction effect has " + effect.getType() +
                            " type but required " + EffectModel.EffectType.SHOW_FACILITY
            );
        }
        this.effectListener.onShowFacility(((ShowFacilityEffectModel) effect).getFacilityUuid());
        this.completeCallback.run();
    }

    /**
     * Triggers the next step in interaction.
     */
    @Override
    public void triggerNext() {
    }
}
