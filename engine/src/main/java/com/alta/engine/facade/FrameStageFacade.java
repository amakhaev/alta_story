package com.alta.engine.facade;

import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.engine.core.storage.EngineStorage;
import com.alta.engine.data.frameStage.JumpingEngineModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import com.google.inject.Inject;
import lombok.NonNull;
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
    private final EngineStorage engineStorage;

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStageView() {
        this.frameStagePresenter.tryToRenderFrameStageView();
        this.messageBoxPresenter.forceHideMessageBox();
        this.messageBoxPresenter.showTitle(this.engineStorage.getFrameStageData().getMapDisplayName());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.frameStagePresenter.startScene();
    }

    /**
     * Performs the participantComputator on scene.
     *
     * @param movementDirection - the participantComputator that should be performed
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

        JumpingEngineModel jumpingPoint = this.engineStorage.getFrameStageData().getJumpingPoints().stream()
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

    /**
     * Replaces the facility on map. One facility will be hidden, second will be shown.
     *
     * @param hideFacility - the facility to be hidden.
     * @param showFacility - the facility to be shown.
     */
    public void replaceFacility(@NonNull String hideFacility, @NonNull String showFacility) {
        this.frameStagePresenter.removeFacility(hideFacility);
        this.frameStagePresenter.addFacility(showFacility);
    }

}
