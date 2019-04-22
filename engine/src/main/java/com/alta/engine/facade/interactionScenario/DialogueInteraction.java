package com.alta.engine.facade.interactionScenario;

import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.engine.model.interaction.DialogueEffectEngineModel;
import com.alta.engine.model.interaction.InteractionEffectEngineModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the interaction that shown the dialogue.
 */
@Slf4j
public class DialogueInteraction implements Interaction {

    private final FrameStagePresenter frameStagePresenter;
    private final MessageBoxPresenter messageBoxPresenter;
    private final TargetedParticipantSummary targetedParticipant;

    private DialogueEffectEngineModel dialogueEffect;

    @Setter
    private Runnable completeCallback;

    /**
     * Initialize new instance of {@link DialogueInteraction}.
     */
    @AssistedInject
    public DialogueInteraction(FrameStagePresenter frameStagePresenter,
                               MessageBoxPresenter messageBoxPresenter,
                               @Assisted @NonNull TargetedParticipantSummary targetedParticipant) {
        this.frameStagePresenter = frameStagePresenter;
        this.messageBoxPresenter = messageBoxPresenter;
        this.targetedParticipant = targetedParticipant;
    }

    /**
     * Starts the interaction.
     *
     * @param effect - the effect to be shown.
     */
    @Override
    public void start(@NonNull InteractionEffectEngineModel effect) {
        if (effect.getType() != InteractionEffectEngineModel.EffectType.DIALOGUE) {
            throw new ClassCastException(
                    "The interaction effect has " + effect.getType() +
                            " type but required " + InteractionEffectEngineModel.EffectType.DIALOGUE
            );
        }
        this.dialogueEffect = (DialogueEffectEngineModel) effect;

        if (!this.messageBoxPresenter.isDialogueBoxOpen()) {
            this.tryToStartInteraction();
        } else {
            log.warn("The dialog already opened");
        }
    }

    /**
     * Triggers the next step in interaction.
     */
    @Override
    public void triggerNext() {
        if (this.messageBoxPresenter.isDialogueBoxOpen()) {
            this.tryToHideMessageBox();
        } else {
            log.warn("The dialog wasn't opened");
        }
    }

    private void tryToHideMessageBox() {
        this.messageBoxPresenter.tryToHideMessageBox();

        if (!this.messageBoxPresenter.isDialogueBoxOpen()) {
            log.debug("Completed dialogue interaction with NPC {}", this.targetedParticipant.getUuid());
            this.frameStagePresenter.stopInteractionWithNpc(this.targetedParticipant.getUuid());

            if (this.completeCallback != null) {
                this.completeCallback.run();
            }
        }
    }

    private void tryToStartInteraction() {
        log.debug("Perform the dialogue interaction. Target participant was found with uuid {}.", this.targetedParticipant.getUuid());
        this.messageBoxPresenter.showDialogueMessage(this.dialogueEffect.getText());

        if (this.targetedParticipant.getParticipatType() == ParticipatType.SIMPLE_NPC) {
            this.frameStagePresenter.startInteractionWithNpc(this.targetedParticipant.getUuid());
        }
    }
}
