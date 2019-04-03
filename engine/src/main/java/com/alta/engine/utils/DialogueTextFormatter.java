package com.alta.engine.utils;

import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;

/**
 * Provides the formatter of text for dialogues.
 */
@UtilityClass
public class DialogueTextFormatter {

    private final static String SUB_MESSAGE_SPLITTER = "#sub#";

    /**
     * Creates the sub messages from given dialogue.
     *
     * @param dialogueText - the initial text of dialogue.
     * @return the array of sub messages from dialogue.
     */
    public String[] createSubMessagesFromDialogue(String dialogueText) {
        if (Strings.isNullOrEmpty(dialogueText)) {
            return null;
        }

        return dialogueText.split(SUB_MESSAGE_SPLITTER);
    }

}
