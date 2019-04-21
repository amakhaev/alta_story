package com.alta.engine.facade;

import com.alta.engine.facade.interactionScenario.InteractionScenario;
import com.alta.engine.facade.interactionScenario.InteractionScenarioFactory;
import com.alta.engine.model.InteractionDataModel;
import com.alta.engine.model.interaction.InteractionEngineModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the facade to cooperation interaction in engine.
 */
@Slf4j
public class InteractionFacade {

    private final FrameStagePresenter frameStagePresenter;
    private final InteractionScenario interactionScenario;
    private final FrameStageListener frameStageListener;

    private InteractionEngineModel currentInteraction;

    @Setter
    private InteractionDataModel interactionData;

    /**
     * Initialize ew instance of {@link InteractionFacade}.
     */
    @Inject
    public InteractionFacade(FrameStagePresenter frameStagePresenter,
                             InteractionScenarioFactory interactionScenarioFactory,
                             FrameStageListener frameStageListener) {
        this.frameStagePresenter = frameStagePresenter;
        this.interactionScenario = interactionScenarioFactory.createInteractionScenario(this::onScenarioCompleted);
        this.frameStageListener = frameStageListener;
    }

    /**
     * Triggers the interaction between acting character and another participant.
     */
    public void triggerInteraction() {
        if (this.interactionData == null) {
            log.error("The interaction data not fount.");
        }

        if (this.currentInteraction != null) {
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

        InteractionEngineModel interaction = this.findInteractionByTargetUuid(targetedUuid);
        if (interaction == null) {
            log.error("Interaction not found for target uuid {}", targetedUuid);
            return;
        }

        this.currentInteraction = interaction;
        this.interactionScenario.performScenario(interaction.getTargetUuid(), interaction.getInteractionEffects());
    }

    private void triggerNextStepOnScenario() {
        this.interactionScenario.onNext();
    }

    private InteractionEngineModel findInteractionByTargetUuid(@NonNull String targetUuid) {
        InteractionEngineModel interaction = this.interactionData.getInteractions()
                .stream()
                .filter(interactionModel -> interactionModel.getTargetUuid().equals(targetUuid))
                .findFirst()
                .orElse(null);

        if (interaction == null) {
            return null;
        }

        InteractionEngineModel incompletedInteraction = interaction.findIncompletedInteraction();
        return incompletedInteraction == null ? interaction.findLastInteraction() : incompletedInteraction;
    }

    private void onScenarioCompleted() {
        if (this.currentInteraction == null) {
            log.warn("Complete interaction was called but no interaction model found.");
            return;
        }

        this.currentInteraction.setCompleted(true);
        this.frameStageListener.handleInteractionCompleteEvent(this.currentInteraction.getUuid());
        this.currentInteraction = null;
    }
}
