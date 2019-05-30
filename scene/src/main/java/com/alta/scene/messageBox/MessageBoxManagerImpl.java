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
    private final MessageBoxEntityImpl bottomMessageBox;

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

        this.bottomMessageBox = MessageBoxEntityImpl.builder()
                .startCoordinates(new Point(0, config.getUiContainer().getHeight() - 137))
                .width(config.getUiContainer().getWidth())
                .height(137)
                .marginLeft(5)
                .marginRight(5)
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
     * Gets the message box that shown on bottom of the scene.
     */
    @Override
    public MessageBoxEntity getBottomMessageBoxEntity() {
        return this.bottomMessageBox;
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

    /**
     * Draws the simple message on the bottom of screen.
     *
     * @param message - the message the should be shown.
     */
    @Override
    public void drawAnimatedMessage(String message) {
        this.bottomMessageBox.setText(message);
        this.bottomMessageBox.show();
    }

    /**
     * Draws the message using animation on the bottom of screen.
     *
     * @param message           - the text that will be shown.
     * @param faceSetDescriptor - the descriptor of face set.
     */
    @Override
    public void drawAnimatedMessage(String message, FaceSetDescriptor faceSetDescriptor) {
        this.bottomMessageBox.setText(message);
        this.bottomMessageBox.setFaceSet(faceSetDescriptor);
        this.bottomMessageBox.show();
    }

    /**
     * Completes the animation of current message immediately.
     */
    @Override
    public void completeCurrentAnimatedMessage() {
        if (this.bottomMessageBox.isDrawingInProgress()) {
            this.bottomMessageBox.completeDrawingImmediately();
        }
    }

    /**
     * Hides the message box.
     */
    @Override
    public void hideMessageBox() {
        this.bottomMessageBox.hide();
    }

    /**
     * Indicates when animation related to text drawing in progress now.
     */
    @Override
    public boolean isAnimationInProgress() {
        return this.bottomMessageBox.isDrawingInProgress();
    }
}
