package com.alta.mediator.sceneModule;

import com.alta.scene.entities.Actor;
import com.alta.scene.entities.FrameStage;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;

public class BaseFrameStage extends FrameStage {

    /**
     * Initialize new instance of {@link FrameStage}
     */
    BaseFrameStage(BaseFrameTemplate frameTemplate, List<Actor> actors) {
        super(frameTemplate, actors);
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
    public void onRenderStage(GameContainer gameContainer, Graphics graphics) {
        this.frameTemplate.getTiledMap().render(this.frameTemplate.getStartPosition().x, this.frameTemplate.getStartPosition().y);
    }
}
