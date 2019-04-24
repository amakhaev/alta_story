package com.alta.engine.facade.interactionScenario;

import com.alta.engine.model.interaction.HideFacilityEffectEngineModel;
import com.alta.engine.model.interaction.InteractionEffectEngineModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.google.inject.assistedinject.AssistedInject;
import lombok.Setter;

/**
 * Provides the interaction that handles the hide of facility.
 */
public class HideFacilityInteraction implements Interaction {

    private final FrameStagePresenter frameStagePresenter;

    @Setter
    private Runnable completeCallback;

    /**
     * Initialize new instance of {@link HideFacilityInteraction}.
     */
    @AssistedInject
    public HideFacilityInteraction(FrameStagePresenter frameStagePresenter) {
        this.frameStagePresenter = frameStagePresenter;
    }

    /**
     * Starts the interaction.
     *
     * @param effect - the effect to be shown.
     */
    @Override
    public void start(InteractionEffectEngineModel effect) {
        if (effect.getType() != InteractionEffectEngineModel.EffectType.HIDE_FACILITY) {
            throw new ClassCastException(
                    "The interaction effect has " + effect.getType() +
                            " type but required " + InteractionEffectEngineModel.EffectType.HIDE_FACILITY
            );
        }
        HideFacilityEffectEngineModel hideEffect = (HideFacilityEffectEngineModel) effect;
        this.frameStagePresenter.removeFacility(hideEffect.getFacilityUuid());
        this.completeCallback.run();
    }

    /**
     * Triggers the next step in interaction.
     */
    @Override
    public void triggerNext() {
    }
}
