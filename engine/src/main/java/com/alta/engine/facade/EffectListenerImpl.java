package com.alta.engine.facade;

import com.alta.behaviorprocess.data.common.FaceSetDescription;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectListener;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import com.alta.engine.data.frameStage.NpcEngineModel;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import com.alta.scene.messageBox.FaceSetDescriptor;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the implementation of listening effects.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EffectListenerImpl implements EffectListener {

    private final FrameStagePresenter frameStagePresenter;
    private final MessageBoxPresenter messageBoxPresenter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShowMessage(String targetUuid, String message) {
        if (this.messageBoxPresenter.isDialogueBoxOpen()) {
            log.warn("The dialog already opened");
            return;
        }

        log.debug("Perform the dialogue interaction. Target participant was found with uuid {}.", targetUuid);
        this.messageBoxPresenter.showDialogueMessage(message);

        // The target participant can has type NPC or FACILITY. If interaction has started with NPC then need to do
        // some actions for it.
        this.frameStagePresenter.startInteractionWithNpc(targetUuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShowMessage(String targetUuid, String message, String speakerUuid, String speakerEmotion) {
        if (Strings.isNullOrEmpty(speakerUuid) || Strings.isNullOrEmpty(speakerEmotion)) {
            this.onShowMessage(targetUuid, message);
            return;
        }

        NpcEngineModel npcEngineModel = this.frameStagePresenter.findNpcByUuid(speakerUuid);
        log.debug("Perform the dialogue interaction. Target participant was found with uuid {}.", targetUuid);
        this.messageBoxPresenter.showDialogueMessage(
                message,
                new FaceSetDescriptor(
                        npcEngineModel.getFaceSetDescriptor().getTileWidth(),
                        npcEngineModel.getFaceSetDescriptor().getTileHeight(),
                        npcEngineModel.getFaceSetDescriptor().getEmotions().get(speakerEmotion).x,
                        npcEngineModel.getFaceSetDescriptor().getEmotions().get(speakerEmotion).y,
                        npcEngineModel.getFaceSetDescriptor().getPathToImageSet()
                )
        );

        // The target participant can has type NPC or FACILITY. If interaction has started with NPC then need to do
        // some actions for it.
        this.frameStagePresenter.startInteractionWithNpc(targetUuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShowMessage(String targetUuid, String message, FaceSetDescription faceSetDescription, String speakerEmotion) {
        if (faceSetDescription == null || Strings.isNullOrEmpty(speakerEmotion)) {
            this.onShowMessage(targetUuid, message);
            return;
        }

        log.debug("Perform the dialogue interaction. Target participant was given with uuid {}.", targetUuid);
        this.messageBoxPresenter.showDialogueMessage(
                message,
                new FaceSetDescriptor(
                        faceSetDescription.getTileWidth(),
                        faceSetDescription.getTileHeight(),
                        faceSetDescription.getEmotions().get(speakerEmotion).x,
                        faceSetDescription.getEmotions().get(speakerEmotion).y,
                        faceSetDescription.getPathToImageSet()
                )
        );

        // The target participant can has type NPC or FACILITY. If interaction has started with NPC then need to do
        // some actions for it.
        this.frameStagePresenter.startInteractionWithNpc(targetUuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTriggerNextStateForMessage(String targetUuid, Runnable completeCallback) {
        if (!this.messageBoxPresenter.isDialogueBoxOpen()) {
            log.warn("The dialog wasn't opened");
            return;

        }

        this.messageBoxPresenter.tryToHideMessageBox();

        if (!this.messageBoxPresenter.isDialogueBoxOpen()) {
            log.debug("Completed dialogue interaction with NPC {}", targetUuid);
            this.frameStagePresenter.stopInteractionWithNpc(targetUuid);

            if (completeCallback != null) {
                completeCallback.run();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onHideFacility(@NonNull String facilityUuid) {
        this.frameStagePresenter.removeFacility(facilityUuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShowFacility(@NonNull String facilityUuid) {
        this.frameStagePresenter.addFacility(facilityUuid);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onRouteMovement(String npcTargetUuid, int x, int y, int movementSpeed, String finalDirection) {
        this.frameStagePresenter.movementPerform(npcTargetUuid, x, y, movementSpeed, MovementDirection.valueOf(finalDirection));
    }
}
