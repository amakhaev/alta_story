package com.alta.scene.entities;

import com.alta.scene.core.RenderableEntity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Provides the massage box that shown the text.
 */
public interface MessageBoxEntity extends RenderableEntity<Graphics> {

    /**
     * Updates the message box
     *
     * @param gameContainer - the game container instance
     * @param delta - the delta between last and current calls
     */
    void onUpdateMessageBox(GameContainer gameContainer, int delta);

}
