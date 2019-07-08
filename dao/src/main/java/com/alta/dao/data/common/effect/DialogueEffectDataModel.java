package com.alta.dao.data.common.effect;

import lombok.Builder;
import lombok.Data;

/**
 * Provides the model that describes the dialogue.
 */
@Data
public class DialogueEffectDataModel extends EffectDataModel {

    @Builder
    private static DialogueEffectDataModel create(String text,
                                                  String speakerName,
                                                  String speakerUuid,
                                                  String speakerEmotion) {
        return new DialogueEffectDataModel(text, speakerName, speakerUuid, speakerEmotion);
    }

    private final String text;
    private final String speakerName;
    private final String speakerUuid;
    private final String speakerEmotion;

    /**
     * Initialize new instance of {@link DialogueEffectDataModel}
     * @param text              - the text to be shown in the dialog.
     * @param speakerName       - the name of speaker. Can be null.
     * @param speakerUuid       - the uuid of speaker. Can be null.
     * @param speakerEmotion    - the emotion of speaker.
     */
    private DialogueEffectDataModel(String text, String speakerName, String speakerUuid, String speakerEmotion) {
        super(InteractionEffectType.DIALOGUE);
        this.text = text;
        this.speakerName = speakerName;
        this.speakerUuid = speakerUuid;
        this.speakerEmotion = speakerEmotion;
    }
}
