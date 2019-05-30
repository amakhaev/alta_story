package com.alta.interaction.scenario;

import com.alta.interaction.data.EffectModel;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * Provides the interaction.scenario of interaction effects.
 */
@Slf4j
public class Scenario {

    private final EffectListener effectListener;
    private final EffectFactory effectFactory;
    private final Runnable successCallback;
    private final Runnable failCallback;

    private Queue<EffectModel> currentScenario;
    private Interaction currentInteraction;
    private String targetedParticipantUuid;

    /**
     * Initialize new instance of {@link Scenario}.
     */
    @AssistedInject
    public Scenario(EffectFactory effectFactory,
                    @Assisted @NonNull EffectListener effectListener,
                    @Assisted("successCallback") Runnable successCallback,
                    @Assisted("failCallback") Runnable failCallback) {
        this.effectListener = effectListener;
        this.currentScenario = new ArrayDeque<>();
        this.effectFactory = effectFactory;
        this.successCallback = successCallback;
        this.failCallback = failCallback;
    }

    /**
     * Performs the interaction.scenario related to interaction.
     *
     * @param targetedParticipantUuid   - the uuid of targeted participant.
     * @param effects                   - the effects of interaction that should be shown.
     */
    public void performScenario(@NonNull String targetedParticipantUuid, @NonNull List<EffectModel> effects) {
        if (effects.size() == 0) {
            this.failCallback.run();
            return;
        }

        if (!this.currentScenario.isEmpty() || this.currentInteraction != null) {
            throw new RuntimeException("Scenario already in progress. Can't add another effects.");
        }

        this.targetedParticipantUuid = targetedParticipantUuid;
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
                    "The interaction.scenario interaction completed. Target uuid: {}",
                    this.targetedParticipantUuid
            );
            this.targetedParticipantUuid = null;
            this.successCallback.run();
            return;
        }

        Interaction interaction = null;
        EffectModel interactionEffect = this.currentScenario.poll();
        switch (interactionEffect.getType()) {
            case DIALOGUE:
                interaction = this.effectFactory.createDialogueInteraction(this.targetedParticipantUuid, this.effectListener);
                break;
            case HIDE_FACILITY:
                interaction = this.effectFactory.createHideFacilityInteraction(this.effectListener);
                break;
            case SHOW_FACILITY:
                interaction = this.effectFactory.createShowFacilityInteraction(this.effectListener);
                break;
            default:
                log.error("Unknown type of interaction: {}", interactionEffect.getType());
                this.failCallback.run();
        }

        if (interaction != null) {
            this.startInteraction(interactionEffect, interaction);
        }
    }

    private void startInteraction(EffectModel interactionEffect, Interaction interaction) {
        this.currentInteraction = interaction;
        this.currentInteraction.setCompleteCallback(() -> {
            this.currentInteraction = null;
            this.determinateInteractionAndStart();
        });
        this.currentInteraction.start(interactionEffect);
    }
}
