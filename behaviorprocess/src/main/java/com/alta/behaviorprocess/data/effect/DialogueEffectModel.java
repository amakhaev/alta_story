package com.alta.behaviorprocess.data.effect;

import com.alta.behaviorprocess.data.common.FaceSetDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the dialogue model.
 */
@Getter
public class DialogueEffectModel extends EffectModel {

    private final String text;
    private final DialogueSpeaker dialogueSpeaker;

    /**
     * Initialize new instance of {@link DialogueEffectModel}.
     *
     * @param text              - the text of dialog.
     * @param dialogueSpeaker   - the dialog speaker.
     */
    public DialogueEffectModel(String text, DialogueSpeaker dialogueSpeaker) {
        super(EffectType.DIALOGUE);
        this.text = text;
        this.dialogueSpeaker = dialogueSpeaker;
    }

    @Getter
    @AllArgsConstructor
    public static class DialogueSpeaker {
        private String uuid;
        private String emotion;
        private FaceSetDescription faceSetDescription;
    }
}
