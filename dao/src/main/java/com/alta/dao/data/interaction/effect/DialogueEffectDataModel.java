package com.alta.dao.data.interaction.effect;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that describes the dialogue.
 */
@Getter
@Setter
public class DialogueEffectDataModel extends InteractionEffectDataModel {

    private String text;

    /**
     * Initialize new instance of {@link DialogueEffectDataModel}
     */
    public DialogueEffectDataModel() {
        super(InteractionEffectType.DIALOGUE);
    }
}
