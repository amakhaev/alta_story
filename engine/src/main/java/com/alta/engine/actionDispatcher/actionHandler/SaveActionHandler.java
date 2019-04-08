package com.alta.engine.actionDispatcher.actionHandler;

import com.alta.engine.eventProducer.EngineEvent;
import com.alta.engine.eventProducer.EngineEventType;
import com.alta.engine.eventProducer.eventPayload.SaveStateEventPayload;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;
import com.alta.engine.utils.dataBuilder.FrameStageData;
import com.alta.eventStream.EventProducer;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.awt.*;

/**
 * Provides the handles of saving action character state.
 */
@Slf4j
public class SaveActionHandler implements ActionHandler {

    private final FrameStagePresenter frameStagePresenter;
    private final EventProducer<EngineEvent> engineEventProducer;

    /**
     * Initialize new instance of {@link SaveActionHandler}.
     */
    @Inject
    public SaveActionHandler(FrameStagePresenter frameStagePresenter,
                             @Named("engineEventProducer") EventProducer<EngineEvent> engineEventProducer) {
        this.frameStagePresenter = frameStagePresenter;
        this.engineEventProducer = engineEventProducer;
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
        if (action != SceneAction.TEMP_SAVE) {
            return;
        }

        FrameStageData frameStageData = this.frameStagePresenter.getCurrentFrameStage();
        Point actionCharacterMapCoordinates = this.frameStagePresenter.getActingCharacterMapCoordinate();

        if (frameStageData == null || actionCharacterMapCoordinates == null) {
            return;
        }

        this.engineEventProducer.publishEvent(
                new EngineEvent(EngineEventType.SAVE_STATE, new SaveStateEventPayload(
                        frameStageData.getMapName(),
                        frameStageData.getActingCharacter().getSkinName(),
                        actionCharacterMapCoordinates
                ))
        );
    }
}
