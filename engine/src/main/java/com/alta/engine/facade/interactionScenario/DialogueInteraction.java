package com.alta.engine.facade.interactionScenario;

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
    private final String targetUuid;

    private DialogueEffectEngineModel dialogueEffect;

    @Setter
    private Runnable completeCallback;

    /**
     * Initialize new instance of {@link DialogueInteraction}.
     */
    @AssistedInject
    public DialogueInteraction(FrameStagePresenter frameStagePresenter,
                               MessageBoxPresenter messageBoxPresenter,
                               @Assisted @NonNull String targetUuid) {
        this.frameStagePresenter = frameStagePresenter;
        this.messageBoxPresenter = messageBoxPresenter;
        this.targetUuid = targetUuid;
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
            log.info("Completed dialogue interaction with NPC {}", this.targetUuid);
            this.frameStagePresenter.stopInteractionWithNpc(this.targetUuid);

            if (this.completeCallback != null) {
                this.completeCallback.run();
            }
        }
    }

    private void tryToStartInteraction() {
        log.info("Perform the dialogue interaction. Target participant was found with uuid {}.", this.targetUuid);
        this.messageBoxPresenter.showDialogueMessage(this.dialogueEffect.getText());

        // The participant with targetedUuid is unknown. For case when it NPC then will be performed some actions
        // otherwise nothing do.
        this.frameStagePresenter.startInteractionWithNpc(this.targetUuid);
    }
}
