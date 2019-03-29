package com.alta.scene.messageBox;

import com.alta.scene.configuration.SceneConfig;
import com.alta.scene.entities.MessageBoxEntity;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.awt.*;

/**
 * Provides the implementation of {@link MessageBoxManager}
 */
@Singleton
public class MessageBoxManagerImpl implements MessageBoxManager {

    private final MessageBoxEntityImpl topMessageBox;

    /**
     * Initialize new instance of {@link MessageBoxManagerImpl}
     */
    @Inject
    public MessageBoxManagerImpl(@Named("sceneConfig") SceneConfig config) {
        this.topMessageBox = MessageBoxEntityImpl.builder()
                .startCoordinates(new Point(0, 0))
                .width(config.getUiContainer().getWidth())
                .height(40)
                .marginLeft(5)
                .marginRight(5)
                .marginTop(5)
                .textAlignment(MessageBoxEntityImpl.TextAlignment.CENTER)
                .build();
    }

    /**
     * Gets the message box that shown on top of the scene.
     */
    @Override
    public MessageBoxEntity getTopMessageBoxEntity() {
        return this.topMessageBox;
    }

    /**
     * Draws the message on scene that will shown as title on the top of screen.
     *
     * @param titleMessage - the text that will be shown.
     */
    @Override
    public void drawTitle(String titleMessage) {
        this.topMessageBox.setText(titleMessage);
        this.topMessageBox.show();
    }

    /**
     * Draws the message on scene that will shown as title on the top of screen.
     *
     * @param titleMessage - the text that will be shown.
     * @param hideTimeout  - the timeout after which title message box will be hidden
     */
    @Override
    public void drawTitle(String titleMessage, int hideTimeout) {
        this.topMessageBox.setText(titleMessage);
        this.topMessageBox.show(hideTimeout);
    }
}
