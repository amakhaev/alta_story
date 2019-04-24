package com.alta.engine.facade.interactionScenario;

import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.engine.model.interaction.HideFacilityEffectEngineModel;
import com.alta.engine.model.interaction.InteractionEffectEngineModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;
import lombok.Setter;

/**
 * Provides the interaction that handles the hide of facility.
 */
public class HideFacilityInteraction implements Interaction {

    private final FrameStagePresenter frameStagePresenter;
    private final TargetedParticipantSummary targetedParticipant;

    private HideFacilityEffectEngineModel hideFacilityEffect;

    @Setter
    private Runnable completeCallback;

    /**
     * Initialize new instance of {@link HideFacilityInteraction}.
     */
    @AssistedInject
    public HideFacilityInteraction(FrameStagePresenter frameStagePresenter,
                                   @Assisted @NonNull TargetedParticipantSummary targetedParticipant) {
        this.frameStagePresenter = frameStagePresenter;
        this.targetedParticipant = targetedParticipant;
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
        this.hideFacilityEffect = (HideFacilityEffectEngineModel) effect;
        this.frameStagePresenter.removeFacility(this.hideFacilityEffect.getFacilityUuid());
        this.completeCallback.run();
    }

    /**
     * Triggers the next step in interaction.
     */
    @Override
    public void triggerNext() {
    }
}
