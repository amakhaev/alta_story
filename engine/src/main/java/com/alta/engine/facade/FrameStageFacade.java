package com.alta.engine.facade;

import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.engine.model.FrameStageDataModel;
import com.alta.engine.model.frameStage.JumpingEngineModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import com.google.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the high level facade to cooperation with frame stage
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrameStageFacade {

    private final FrameStagePresenter frameStagePresenter;
    private final MessageBoxPresenter messageBoxPresenter;

    @Getter
    private FrameStageDataModel frameStageDataModel;

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStageView(FrameStageDataModel data) {
        this.frameStageDataModel = data;
        this.frameStagePresenter.tryToRenderFrameStageView(this.frameStageDataModel);
        this.messageBoxPresenter.forceHideMessageBox();
        this.messageBoxPresenter.showTitle(data.getMapDisplayName());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.frameStagePresenter.startScene();
    }

    /**
     * Performs the movement on scene.
     *
     * @param movementDirection - the movement that should be performed
     */
    public void movementPerform(MovementDirection movementDirection) {
        this.frameStagePresenter.movementPerform(movementDirection);
    }

    /**
     * Gets the map coordinates of acting character.
     */
    public Point getActingCharacterMapCoordinate() {
        return this.frameStagePresenter.getActingCharacterMapCoordinate();
    }

    /**
     * Finds the jumping point from existing one.
     *
     * @param mapCoordinates - the jump point to be find.
     * @return found {@link JumpingEngineModel} point.
     */
    public JumpingEngineModel findJumpingPoint(Point mapCoordinates) {
        if (mapCoordinates == null) {
            log.error("The coordinates of map to be jumped is null");
            return null;
        }

        JumpingEngineModel jumpingPoint = this.frameStageDataModel.getJumpingPoints().stream()
                .filter(jp -> jp.getFrom().x == mapCoordinates.x && jp.getFrom().y == mapCoordinates.y)
                .findAny()
                .orElse(null);

        if (jumpingPoint == null) {
            log.warn("Jump point for {} not found.", mapCoordinates);
        } else {
            log.info("Jump point for given coordinates {} found, map name: {}.", mapCoordinates, jumpingPoint.getMapName());
        }

        return jumpingPoint;
    }

}
