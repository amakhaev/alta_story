package com.alta.scene;

import com.alta.scene.entities.FrameStage;
import com.alta.scene.messageBox.MessageBox;
import com.alta.scene.messageBox.MessageBoxManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

/**
 * Provides the base scene object
 */
@Slf4j
public class Scene {

    private final AppGameContainer gameContainer;
    private final SceneContainer sceneContainer;
    private final MessageBoxManager messageBoxManager;

    @Getter
    private boolean isSceneInitialized;

    /**
     * Initialize new instance of {@link Scene}
     */
    public Scene() {
        Injector injector = Guice.createInjector(new SceneInjectorModule());
        this.gameContainer = injector.getInstance(AppGameContainer.class);
        // this.gameContainer.setMouseGrabbed(true);
        this.sceneContainer = injector.getInstance(SceneContainer.class);
        this.messageBoxManager = injector.getInstance(MessageBoxManager.class);
        this.isSceneInitialized = false;
    }

    /**
     * Starts the scene.
     */
    public void start() {
        try {
            this.isSceneInitialized = true;
            this.gameContainer.start();
        } catch (SlickException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Sets the listener of input on scene.
     *
     * @param listener - the listener instance.
     */
    public void setInputListener(KeyListener listener) {
        this.sceneContainer.setInputListener(listener);
    }

    /**
     * Sets the frame stage to render
     *
     * @param frameStage - the stage to render.
     */
    public void renderStage(FrameStage frameStage) {
        this.sceneContainer.renderStage(frameStage);
    }

    /**
     * Indicates when scene (widow) has a focus.
     *
     * @return true if scene has a focus, false otherwise.
     */
    public boolean hasFocus() {
        return this.gameContainer.hasFocus();
    }

    /**
     * Gets the message box instance.
     */
    public MessageBox getMessageBox() {
        return this.messageBoxManager;
    }
}
