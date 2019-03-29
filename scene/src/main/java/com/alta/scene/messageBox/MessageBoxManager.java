package com.alta.scene.messageBox;

import com.alta.scene.entities.MessageBoxEntity;

/**
 * provides the container for {@link com.alta.scene.entities.MessageBoxEntity}
 */
public interface MessageBoxManager extends MessageBox {

    /**
     * Gets the message box that shown on top of the scene.
     */
    MessageBoxEntity getTopMessageBoxEntity();

}
