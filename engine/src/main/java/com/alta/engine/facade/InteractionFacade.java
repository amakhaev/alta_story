package com.alta.engine.facade;

import com.alta.engine.facade.interactionScenario.InteractionScenario;
import com.alta.engine.model.InteractionDataModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the facade to cooperation interaction in engine.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InteractionFacade {

    private final FrameStagePresenter frameStagePresenter;
    private final InteractionScenario interactionScenario;

    @Setter
    private InteractionDataModel interactionData;

    /**
     * Triggers the interaction between acting character and another participant.
     */
    public void triggerInteraction() {
        if (this.interactionData == null) {
            log.error("The interaction data not fount.");
        }

        if (this.interactionScenario.isInProgress()) {
            this.triggerNextStepOnScenario();
        } else {
            this.startInteraction();
        }
    }

    private void startInteraction() {
        String targetedUuid = this.frameStagePresenter.findSimpleNpcTargetedByActingCharacter();
        if (Strings.isNullOrEmpty(targetedUuid)) {
            log.info("Participant for interaction not found.");
            return;
        }

        this.interactionData.getInteractions()
                .stream()
                .filter(interactionModel -> interactionModel.getTargetUuid().equals(targetedUuid))
                .findFirst()
                .ifPresent(interactionEngineModel -> this.interactionScenario.performScenario(
                        interactionEngineModel.getTargetUuid(), interactionEngineModel.getInteractionEffects())
                );

    }

    private void triggerNextStepOnScenario() {
        this.interactionScenario.onNext();
    }
}
