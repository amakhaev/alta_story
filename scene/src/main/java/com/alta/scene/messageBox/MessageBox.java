package com.alta.scene.messageBox;

/**
 * Provides the interface to render any messages
 */
public interface MessageBox {

    /**
     * Draws the message on scene that will shown as title on the top of screen.
     *
     * @param titleMessage  - the text that will be shown.
     * @param hideTimeout   - the timeout after which title message box will be hidden
     */
    void drawTitle(String titleMessage, int hideTimeout);

    /**
     * Draws the message using animation on the bottom of screen.
     *
     * @param message - the message the should be shown.
     */
    void drawAnimatedMessage(String message);

    /**
     * Draws the message using animation on the bottom of screen.
     *
     * @param message           - the text that will be shown.
     * @param faceSetDescriptor - the descriptor of face set.
     */
    void drawAnimatedMessage(String message, FaceSetDescriptor faceSetDescriptor);

    /**
     * Completes the animation of current message immediately.
     */
    void completeCurrentAnimatedMessage();

    /**
     * Hides the message box.
     */
    void hideMessageBox();

    /**
     * Indicates when animation related to text drawing in progress now.
     */
    boolean isAnimationInProgress();
}
