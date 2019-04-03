package com.alta.engine.view;

import com.alta.scene.messageBox.MessageBox;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the view that can show message boxes
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MessageBoxView {

    private static final int HIDE_TITLE_MESSAGE_BOX_TIMEOUT = 5000;

    private final MessageBox messageBox;

    /**
     * Shows the title message on the top of scene.
     *
     * @param title - the title that should be shown.
     */
    public void showTitle(String title) {
        this.messageBox.drawTitle(title, HIDE_TITLE_MESSAGE_BOX_TIMEOUT);
    }

    /**
     * Shown the message on the bottom of scene.
     *
     * @param text - the text to be shown.
     */
    public void showMessage(String text) {
        this.messageBox.drawAnimatedMessage(text);
    }

    /**
     * Completes the animation of current message immediately.
     */
    public void completeAnimationImmediately() {
        this.messageBox.completeCurrentAnimatedMessage();
    }

    /**
     * Hides the message box.
     */
    public void hide() {
        this.messageBox.hideMessageBox();
    }

    /**
     * Indicates when animation related to text drawing in progress now.
     */
    public boolean isCurrentMessageDrawing() {
        return this.messageBox.isAnimationInProgress();
    }
}
