package com.alta.interaction.data;

import lombok.Getter;

/**
 * Provides the dialogue model.
 */
@Getter
public class DialogueEffectModel extends EffectModel {

    private final String text;

    /**
     * Initialize new instance of {@link DialogueEffectModel}.
     *
     * @param text - the text of dialog.
     */
    public DialogueEffectModel(String text) {
        super(EffectType.DIALOGUE);
        this.text = text;
    }
}
