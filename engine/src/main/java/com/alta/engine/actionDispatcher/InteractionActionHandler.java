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
    }
}
