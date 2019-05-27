package com.alta.engine.facade;

import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import com.alta.interaction.scenario.EffectListener;
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
}
