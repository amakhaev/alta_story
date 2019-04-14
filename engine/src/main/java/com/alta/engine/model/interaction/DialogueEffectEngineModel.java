package com.alta.engine.model.interaction;

import lombok.Getter;

/**
 * Provides the dialogue model.
 */
@Getter
public class DialogueEffectEngineModel extends InteractionEffectEngineModel {

    private final String text;

    /**
     * Initialize new instance of {@link DialogueEffectEngineModel}.
     *
     * @param text - the text of dialog.
     */
    public DialogueEffectEngineModel(String text) {
        super(EffectType.DIALOGUE);
        this.text = text;
    }
}
