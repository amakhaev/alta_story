package com.alta.engine.facade.interactionScenario;

import com.alta.engine.model.interaction.InteractionEffectEngineModel;
import com.alta.engine.model.interaction.ShowFacilityEffectEngineModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.google.inject.assistedinject.AssistedInject;
import lombok.Setter;

/**
 * Provides the interaction that handles the show of facility.
 */
public class ShowFacilityInteraction implements Interaction {

    private final FrameStagePresenter frameStagePresenter;

    @Setter
    private Runnable completeCallback;

    /**
     * Initialize new instance of {@link ShowFacilityInteraction}.
     */
    @AssistedInject
    public ShowFacilityInteraction(FrameStagePresenter frameStagePresenter) {
        this.frameStagePresenter = frameStagePresenter;
    }

    /**
     * Starts the interaction.
     *
     * @param effect - the effect to be shown.
     */
    @Override
    public void start(InteractionEffectEngineModel effect) {
        if (effect.getType() != InteractionEffectEngineModel.EffectType.SHOW_FACILITY) {
            throw new ClassCastException(
                    "The interaction effect has " + effect.getType() +
                            " type but required " + InteractionEffectEngineModel.EffectType.SHOW_FACILITY
            );
        }
        ShowFacilityEffectEngineModel showFacilityEffect = (ShowFacilityEffectEngineModel) effect;
        this.frameStagePresenter.addFacility(showFacilityEffect.getFacilityUuid());
        this.completeCallback.run();
    }

    /**
     * Triggers the next step in interaction.
     */
    @Override
    public void triggerNext() {
    }
}
