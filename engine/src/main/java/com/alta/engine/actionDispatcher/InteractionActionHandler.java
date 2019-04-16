package com.alta.engine.actionDispatcher;

import com.alta.engine.facade.InteractionFacade;
import com.alta.engine.presenter.sceneProxy.sceneInput.SceneAction;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the interaction that executes interaction between components (actor, facility etc.) on stage.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InteractionActionHandler implements ActionHandler {

    private final InteractionFacade interactionFacade;

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
        if (action != SceneAction.INTERACTION) {
            return;
        }

        this.interactionFacade.triggerInteraction();

        /*if (this.messageBoxPresenter.isDialogueBoxOpen()) {
            this.tryToHideMessageBox();
        } else {
            this.tryToStartInteraction();
        }*/
    }

    /*private void tryToHideMessageBox() {
        this.messageBoxPresenter.tryToHideMessageBox();

        if (!this.messageBoxPresenter.isDialogueBoxOpen()) {
            log.info("Completed interaction with NPC {}", this.interactionTargetUuid);
            this.frameStagePresenter.stopInteractionWithNpc(this.interactionTargetUuid);
            this.interactionTargetUuid = null;
        }
    }

    private void tryToStartInteraction() {
        SimpleNpcEngineModel simpleNpcEngineModel = this.frameStagePresenter.findSimpleNpcTargetedByActingCharacter();
        if (simpleNpcEngineModel == null || Strings.isNullOrEmpty(simpleNpcEngineModel.getDialogue())) {
            return;
        }

        log.info("Perform the interaction. Target NPC was found with uuid {}.", simpleNpcEngineModel.getUuid());
        this.interactionTargetUuid = simpleNpcEngineModel.getUuid();
        this.messageBoxPresenter.showDialogueMessage(simpleNpcEngineModel.getDialogue());
        this.frameStagePresenter.startInteractionWithNpc(this.interactionTargetUuid);
    }*/
}
