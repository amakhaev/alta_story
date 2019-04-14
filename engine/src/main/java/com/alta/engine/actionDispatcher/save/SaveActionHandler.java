package com.alta.engine.actionDispatcher.save;

import com.alta.engine.actionDispatcher.ActionHandler;
import com.alta.engine.facade.FrameStageListener;
import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the handles of saving action character state.
 */
@Slf4j
public class SaveActionHandler implements ActionHandler {

    private final FrameStageListener frameStageListener;

    /**
     * Initialize new instance of {@link SaveActionHandler}.
     */
    @Inject
    public SaveActionHandler(FrameStageListener frameStageListener) {
        this.frameStageListener = frameStageListener;
    }

    /**
     * Handles the constantly action from scene.
     *
     * @param action - the action to be handled.
     */
    @Override
    public void onHandleConstantlyAction(SceneAction action) {
        log.warn("No need to handle constantly actions here, method shouldn't be called.");
    }

    /**
     * Handles the release action from scene.
     *
     * @param action - the action to be handled.
     */
    @Override
    public void onHandleReleaseAction(SceneAction action) {
        if (action == SceneAction.TEMP_SAVE) {
            this.frameStageListener.handleSaveEvent();
        }

        /*FrameStageDataModel frameStageDataModel = this.frameStagePresenter.getCurrentFrameStage();
        Point actionCharacterMapCoordinates = this.frameStagePresenter.getActingCharacterMapCoordinate();

        if (frameStageDataModel == null || actionCharacterMapCoordinates == null) {
            return;
        }

        this.engineEventProducer.publishEvent(
                new EngineEvent(EngineEventType.SAVE_STATE, new SaveStateEventPayload(
                        frameStageDataModel.getMapName(),
                        frameStageDataModel.getActingCharacter().getSkinName(),
                        actionCharacterMapCoordinates
                ))
        );*/
    }
}
