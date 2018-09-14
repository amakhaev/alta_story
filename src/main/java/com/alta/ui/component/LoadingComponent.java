package com.alta.ui.component;

import com.alta.ui.BaseUIComponent;
import com.alta.utils.ResourceUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

/**
 * Provides the loading screen component.
 */
public class LoadingComponent extends BaseUIComponent {

    private TrueTypeFont ttf;
    private String message;
    private int time;
    private String lastAnimatedMessage;

    /**
     * Init the UI component.
     *
     * @param gameContainer - the game componentContainer.
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        this.ttf = new TrueTypeFont(new Font("Verdana", Font.BOLD, 25), true);
        this.message = ResourceUtils.getString("Loading");
    }

    /**
     * Updates the component.
     *
     * @param gameContainer - the game componentContainer.
     * @param delta - the last update delta
     */
    @Override
    public void onUpdateForInProgressState(GameContainer gameContainer, int delta) throws SlickException {
        this.time += delta;
    }

    /**
     * Renders the component whe it has IN_PROGRESS state.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    public void onRenderForInProgressState(GameContainer gameContainer, Graphics graphics) throws SlickException {
        this.lastAnimatedMessage = this.createAnimatedMessage();

        this.ttf.drawString(
                gameContainer.getWidth() / 2 - this.ttf.getWidth(this.message) / 2,
                gameContainer.getHeight() / 2 - this.ttf.getLineHeight(),
                this.lastAnimatedMessage
        );
    }

    /**
     * Renders the component whe it has READY state.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    public void onRenderForReadyState(GameContainer gameContainer, Graphics graphics) throws SlickException {
        this.ttf.drawString(
                gameContainer.getWidth() / 2 - this.ttf.getWidth(this.message) / 2,
                gameContainer.getHeight() / 2 - this.ttf.getLineHeight(),
                this.message
        );
    }

    /**
     * Renders the component whe it has IN_PROGRESS state.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    public void onRenderForDetachState(GameContainer gameContainer, Graphics graphics) throws SlickException {
        this.ttf.drawString(
                gameContainer.getWidth() / 2 - this.ttf.getWidth(this.message) / 2,
                gameContainer.getHeight() / 2 - this.ttf.getLineHeight(),
                this.lastAnimatedMessage
        );
    }

    /**
     * Destroy the component.
     */
    @Override
    public void destroy() {
        this.ttf = null;
        this.message = null;
        super.destroy();
    }

    private String createAnimatedMessage() {
        if (this.time < 500) {
            return this.message;
        }
        else if (this.time >= 500 && this.time < 1000) {
            return this.message + ".";
        }
        else if (this.time >= 1000 && this.time < 1500) {
            return this.message + "..";
        }
        else if (this.time >= 1500 && this.time < 2000) {
            return this.message + "...";
        }
        else {
            this.time = 0;
            return this.message;
        }
    }
}
