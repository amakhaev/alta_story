package com.alta.mediator.sceneModule;

import com.alta.computator.altitudeMap.AltitudeMap;
import com.alta.scene.entities.Actor;
import com.alta.scene.entities.FrameStage;
import com.alta.utils.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;

@Slf4j
public class BaseFrameStage extends FrameStage {

    private final ThreadPoolExecutor threadPoolExecutor;
    private AltitudeMap altitudeMap;

    /**
     * Initialize new instance of {@link FrameStage}
     */
    BaseFrameStage(BaseFrameTemplate frameTemplate, List<Actor> actors, ThreadPoolExecutor threadPoolExecutor) {
        super(frameTemplate, actors);
        this.threadPoolExecutor = threadPoolExecutor;
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

    /**
     * Initializes frame stage in GL context
     *
     * @param gameContainer - the game container instance
     */
    @Override
    public void onInit(GameContainer gameContainer) {
        super.onInit(gameContainer);
        this.threadPoolExecutor.run(
                "Create altitude map",
                () -> {
                    this.altitudeMap = new AltitudeMap(this.frameTemplate.getTiledMap());
                    log.debug("Completed creating of altitude ");
                }
        );
    }
}
