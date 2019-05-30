package com.alta.dao.data.interaction.effect;

import lombok.Builder;
import lombok.Getter;

/**
 * Provides the model that describes the dialogue.
 */
@Getter
public class DialogueEffectDataModel extends InteractionEffectDataModel {

    @Builder
    private static DialogueEffectDataModel create(String text, String speakerUuid, String speakerEmotion) {
        return new DialogueEffectDataModel(text, speakerUuid, speakerEmotion);
    }

    private final String text;
    private final String speakerUuid;
    private final String speakerEmotion;

    /**
     * Initialize new instance of {@link DialogueEffectDataModel}
     * @param text              - the text to be shown in the dialog.
     * @param speakerUuid       - the uuid of speaker. Ca be null.
     * @param speakerEmotion    - the emotion of speaker.
     */
    private DialogueEffectDataModel(String text, String speakerUuid, String speakerEmotion) {
        super(InteractionEffectType.DIALOGUE);
        this.text = text;
        this.speakerUuid = speakerUuid;
        this.speakerEmotion = speakerEmotion;
    }
}
