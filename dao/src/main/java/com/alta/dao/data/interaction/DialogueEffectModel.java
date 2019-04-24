package com.alta.dao.data.interaction;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that describes the dialogue.
 */
@Getter
@Setter
public class DialogueEffectModel extends InteractionEffectModel {

    private String text;

    /**
     * Initialize new instance of {@link DialogueEffectModel}
     */
    public DialogueEffectModel() {
        super(InteractionEffectType.DIALOGUE);
    }
}
