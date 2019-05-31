package com.alta.scene.messageBox;

import com.alta.scene.entities.MessageBoxFrame;

/**
 * provides the container for {@link MessageBoxFrame}
 */
public interface MessageBoxManager extends MessageBox {

    /**
     * Gets the message box that shown on top of the scene.
     */
    MessageBoxFrame getTopMessageBoxEntity();

    /**
     * Gets the message box that shown on bottom of the scene.
     */
    MessageBoxFrame getBottomMessageBoxEntity();

}
