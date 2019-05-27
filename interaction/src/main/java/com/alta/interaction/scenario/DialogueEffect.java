package com.alta.interaction.scenario;

import com.alta.interaction.data.DialogueEffectModel;
import com.alta.interaction.data.EffectModel;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the interaction that shown the dialogue.
 */
@Slf4j
public class DialogueEffect implements Interaction {

    private final String targetedParticipantUuid;
    private final EffectListener effectListener;

    @Setter
    private Runnable completeCallback;

    /**
     * Initialize new instance of {@link DialogueEffect}.
     *
     * @param targetedParticipantUuid   - the uuid of targeted participant.
     * @param effectListener            - the listener of effects.
     */
    @AssistedInject
    public DialogueEffect(@Assisted @NonNull String targetedParticipantUuid,
                          @Assisted @NonNull EffectListener effectListener) {
        this.targetedParticipantUuid = targetedParticipantUuid;
        this.effectListener = effectListener;
    }

    /**
     * Starts the interaction.
     *
     * @param effect - the effect to be performed.
     */
    @Override
    public void start(@NonNull EffectModel effect) {
        if (effect.getType() != EffectModel.EffectType.DIALOGUE) {
            throw new ClassCastException(
                    "The interaction effect has " + effect.getType() +
                            " type but required " + EffectModel.EffectType.DIALOGUE
            );
        }
        log.debug("Perform the dialogue interaction. Target participant was found with uuid {}.", this.targetedParticipantUuid);
        this.effectListener.onShowMessage(this.targetedParticipantUuid, ((DialogueEffectModel) effect).getText());
    }

    /**
     * Triggers the next step in interaction.
     */
    @Override
    public void triggerNext() {
        this.effectListener.onTriggerNextStateForMessage(
                this.targetedParticipantUuid,
                () -> {
                    if (this.completeCallback != null) {
                        this.completeCallback.run();
                    }
                }
        );
    }
}
