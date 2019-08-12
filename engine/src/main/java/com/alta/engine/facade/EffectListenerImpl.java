package com.alta.engine.facade;

import com.alta.behaviorprocess.data.common.FaceSetDescription;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectListener;
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
     * Shows the message.
     *
     * @param targetUuid - the uuid of target NPC.
     * @param message    - the message to be shown.
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
     * Shows the message.
     *
     * @param targetUuid     - the uuid of target NPC.
     * @param message        - the message to be shown.
     * @param speakerUuid    - the uuid of speaker.
     * @param speakerEmotion - the emotion that should be shown when speaker say.
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
     * Shows the message.
     *
     * @param targetUuid         - the uuid of target NPC.
     * @param message            - the message to be shown.
     * @param faceSetDescription - the descriptor of face set.
     * @param speakerEmotion     - the emotion that should be shown when speaker say.
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
     * Triggers the state of message box to next one.
     *
     * @param targetUuid       - the uuid of target NPC.
     * @param completeCallback - the callback to be invoked after complete showing of message.
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
     * Hides the facility with given uuid.
     *
     * @param facilityUuid - the uuid of facility to be hide.
     */
    @Override
    public void onHideFacility(@NonNull String facilityUuid) {
        this.frameStagePresenter.removeFacility(facilityUuid);
    }

    /**
     * Shows the facility with given uuid.
     *
     * @param facilityUuid - the uuid of facility to be show.
     */
    @Override
    public void onShowFacility(@NonNull String facilityUuid) {
        this.frameStagePresenter.addFacility(facilityUuid);
    }

    /**
     * Runs the movement process for NPC with given UUID.
     *
     * @param npcTargetUuid - the UUID of NPC to be moved.
     * @param x             - the target X coordinate.
     * @param y             - the target Y coordinate.
     */
    @Override
    public void onRouteMovement(String npcTargetUuid, int x, int y) {
        this.frameStagePresenter.movementPerform(npcTargetUuid, x, y);
    }
}
