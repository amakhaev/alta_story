package com.alta.engine.presenter;

import com.alta.engine.core.customException.EngineException;
import com.alta.engine.utils.DialogueTextFormatter;
import com.alta.engine.view.MessageBoxView;
import com.alta.scene.messageBox.FaceSetDescriptor;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Provides the presenter relate to {@link com.alta.engine.view.MessageBoxView}
 */
@Slf4j
public class MessageBoxPresenter {

    private final MessageBoxView messageBoxView;

    private Queue<String> subMessages;

    @Getter
    private boolean isDialogueBoxOpen;

    /**
     * Initialize new instance of {@link MessageBoxPresenter}
     */
    @Inject
    public MessageBoxPresenter(MessageBoxView messageBoxView) {
        this.messageBoxView = messageBoxView;
        this.isDialogueBoxOpen = false;
        this.subMessages = new ArrayDeque<>();
    }

    /**
     * Shows the title message on the top of scene.
     *
     * @param title - the title that should be shown.
     */
    public void showTitle(String title) {
        this.messageBoxView.showTitle(title);
    }

    /**
     * Shown the message on the bottom of scene.
     *
     * @param message - the message to be shown.
     */
    public void showDialogueMessage(String message) {
        if (Strings.isNullOrEmpty(message)) {
            log.error("Given message is null or empty but required for message box.");
            throw new EngineException("Can't show the dialogue because message is null.");
        }

        String[] subMessages = DialogueTextFormatter.createSubMessagesFromDialogue(message);
        if (subMessages == null) {
            return;
        }

        this.subMessages.addAll(Arrays.asList(subMessages));
        this.messageBoxView.showMessage(this.subMessages.poll());
        this.isDialogueBoxOpen = true;
    }

    /**
     * Shown the message on the bottom of scene.
     *
     * @param message           - the message to be shown.
     * @param faceSetDescriptor - the descriptor of face set.
     */
    public void showDialogueMessage(String message, FaceSetDescriptor faceSetDescriptor) {
        if (Strings.isNullOrEmpty(message)) {
            log.error("Given message is null or empty but required for message box.");
            throw new EngineException("Can't show the dialogue because message is null.");
        }

        String[] subMessages = DialogueTextFormatter.createSubMessagesFromDialogue(message);
        if (subMessages == null) {
            return;
        }

        this.subMessages.addAll(Arrays.asList(subMessages));
        this.messageBoxView.showMessage(this.subMessages.poll(), faceSetDescriptor);
        this.isDialogueBoxOpen = true;
    }

    /**
     * Tries to hide the message box.
     */
    public void tryToHideMessageBox() {
        if (this.messageBoxView.isCurrentMessageDrawing()) {
            this.messageBoxView.completeAnimationImmediately();
        } else if (this.subMessages.size() > 0) {
            this.messageBoxView.showMessage(this.subMessages.poll());
        } else {
            this.isDialogueBoxOpen = false;
            this.messageBoxView.hide();
        }
    }

    /**
     * Hides the message box force.
     */
    public void forceHideMessageBox() {
        this.subMessages.clear();
        this.messageBoxView.hide();
    }
}
