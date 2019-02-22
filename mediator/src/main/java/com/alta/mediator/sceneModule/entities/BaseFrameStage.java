package com.alta.mediator.sceneModule.entities;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.service.movement.StageComputator;
import com.alta.scene.entities.Actor;
import com.alta.scene.entities.FrameStage;
import com.alta.utils.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.awt.*;
import java.util.List;

/**
 * Provides base implementation of frame stage
 */
@Slf4j
public class BaseFrameStage extends FrameStage {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final StageComputator stageComputator;

    /**
     * Initialize new instance of {@link FrameStage}
     */
    public BaseFrameStage(BaseFrameTemplate frameTemplate,
                          List<Actor> actors,
                          StageComputator stageComputator,
                          ThreadPoolExecutor threadPoolExecutor) {
        super(frameTemplate, actors);
        this.threadPoolExecutor = threadPoolExecutor;
        this.stageComputator = stageComputator;
    }

    /**
     * Updates the stage
     *
     * @param gameContainer - the game container instance
     * @param delta         - the delta between last and current calls
     */
    @Override
    public void onUpdateStage(GameContainer gameContainer, int delta) {
        this.stageComputator.onTick();
    }

    /**
     * Renders the stage
     *
     * @param gameContainer - the game container instance
     * @param graphics      - the graphic to render primitives
     */
    @Override
    public void onRenderStage(GameContainer gameContainer, Graphics graphics) {
        // this.frameTemplate.getTiledMap().render(this.frameTemplate.getStartPosition().x, this.frameTemplate.getStartPosition().y);
        this.renderFrame();
        this.renderFocusPoint(graphics);
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
                "Initialize computator for scene",
                () -> {
                    this.stageComputator.setAltitudeMap(
                            new AltitudeMap(this.frameTemplate.getTiledMap(),
                                    gameContainer.getWidth(),
                                    gameContainer.getHeight()
                            )
                    );
                    log.debug("Completed initialization of computator");
                }
        );
    }

    private void renderFocusPoint(Graphics graphics) {
        Point focusPointCoordinates = this.stageComputator.getFocusPointGlobalCoordinates();
        if (focusPointCoordinates == null || this.stageComputator.getAltitudeMap() == null) {
            return;
        }

        graphics.setColor(Color.red);
        graphics.fillRect(
                focusPointCoordinates.x,
                focusPointCoordinates.y,
                this.stageComputator.getAltitudeMap().getTileWidth(),
                this.stageComputator.getAltitudeMap().getTileHeight()
        );
    }

    private void renderFrame() {
        Point mapCoordinates = this.stageComputator.getMapGlobalCoordinates();
        if (mapCoordinates == null) {
            return;
        }

        this.frameTemplate.getTiledMap().render(
                mapCoordinates.x,
                mapCoordinates.y
        );
    }
}
