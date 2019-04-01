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
     * Draws the simple message on the bottom of screen.
     *
     * @param message - the message the should be shown.
     */
    void drawSimpleMessage(String message);
}
