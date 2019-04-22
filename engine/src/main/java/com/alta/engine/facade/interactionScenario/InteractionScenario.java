package com.alta.engine.facade.interactionScenario;

import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.engine.model.interaction.InteractionEffectEngineModel;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * Provides the scenario of interaction effects.
 */
@Slf4j
public class InteractionScenario {

    private final InteractionFactory interactionFactory;
    private final Runnable completeCallback;

    private Queue<InteractionEffectEngineModel> currentScenario;
    private Interaction currentInteraction;
    private TargetedParticipantSummary targetedParticipantSummary;

    /**
     * Initialize new instance of {@link InteractionScenario}.
     */
    @AssistedInject
    public InteractionScenario(InteractionFactory interactionFactory, @Assisted Runnable completeCallback) {
        this.currentScenario = new ArrayDeque<>();
        this.interactionFactory = interactionFactory;
        this.completeCallback = completeCallback;
    }

    /**
     * Performs the scenario related to interaction.
     *
     * @param targetedParticipantSummary    - the summery of participant that selected for interaction.
     * @param effects                       - the effects of interaction that should be shown.
     */
    public void performScenario(@NonNull TargetedParticipantSummary targetedParticipantSummary,
                                @NonNull List<InteractionEffectEngineModel> effects) {
        if (effects.size() == 0) {
            return;
        }

        if (!this.currentScenario.isEmpty() || this.currentInteraction != null) {
            throw new RuntimeException("Scenario already in progress. Can't add another effects.");
        }

        this.targetedParticipantSummary = targetedParticipantSummary;
        this.currentScenario.addAll(effects);
        this.determinateInteractionAndStart();
    }

    /**
     * Handles the next state of effect or switch to next interaction effect.
     */
    public void onNext() {
        if (this.currentInteraction != null) {
            this.currentInteraction.triggerNext();
        }
    }

    private void determinateInteractionAndStart() {
        if (this.currentInteraction != null) {
            log.warn(
                    "{} interaction already in progress. No any action will be performed",
                    this.currentInteraction.getClass().getSimpleName()
            );
            return;
        }

        if (this.currentScenario.peek() == null) {
            log.debug(
                    "The scenario interaction completed. Target type: {}, uuid: {}",
                    this.targetedParticipantSummary.getParticipatType(),
                    this.targetedParticipantSummary.getUuid()
            );
            this.targetedParticipantSummary = null;
            this.completeCallback.run();
            return;
        }

        InteractionEffectEngineModel interaction = this.currentScenario.poll();
        switch (interaction.getType()) {
            case DIALOGUE:
                this.startDialogueInteraction(interaction);
                break;
            default:
                log.error("Unknown type of interaction: {}", interaction.getType());
        }
    }

    private void startDialogueInteraction(InteractionEffectEngineModel interaction) {
        this.currentInteraction = this.interactionFactory.createDialogueInteraction(this.targetedParticipantSummary);
        this.currentInteraction.setCompleteCallback(() -> {
            this.currentInteraction = null;
            this.determinateInteractionAndStart();
        });
        this.currentInteraction.start(interaction);
    }

}
