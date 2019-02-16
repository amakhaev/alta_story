package com.alta.mediator.sceneModule;

import com.alta.scene.frameStorage.FrameStage;
import com.alta.scene.frameStorage.FrameTemplate;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class FrameStageImpl extends FrameStage {


    /**
     * Initialize new instance of {@link FrameStage}
     */
    public FrameStageImpl(FrameTemplate frameTemplate) {
        super(frameTemplate);
    }

    /**
     * Updates the stage
     *
     * @param gameContainer - the game container instance
     * @param delta         - the delta between last and current calls
     */
    @Override
    public void onUpdateStage(GameContainer gameContainer, int delta) {

    }

    /**
     * Renders the stage
     *
     * @param gameContainer - the game container instance
     * @param graphics      - the graphic to render primitives
     */
    @Override
    public void onRenderFrame(GameContainer gameContainer, Graphics graphics) {
        this.frameTemplate.getTiledMap().render(this.frameTemplate.getStartPosition().x, this.frameTemplate.getStartPosition().y);
    }

    /**
     * Initializes frame stage in GL context
     *
     * @param gameContainer - the game container instance
     */
    @Override
    public void onInit(GameContainer gameContainer) {
    }
}
