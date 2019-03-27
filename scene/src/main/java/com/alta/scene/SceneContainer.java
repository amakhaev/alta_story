package com.alta.scene;

import com.alta.scene.configuration.SceneConfig;
import com.alta.scene.core.states.FrameStageState;
import com.alta.scene.core.states.StateManager;
import com.alta.scene.entities.FrameStage;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;

import javax.inject.Named;

@Singleton
@Slf4j
public class SceneContainer extends StateBasedGame {

    private final StateManager stateManager;

    private KeyListener inputListener;
    private boolean isInputListenerChanged;
    private int currentStateId;
    private Transition fadeOutTransition;
    private Transition fadeInTransition;

    /**
     * Initialize new instance of {@link SceneContainer}.
     */
    @Inject
    public SceneContainer(@Named("sceneConfig") SceneConfig config, StateManager stateManager) {
        super(config.getApp().getName());
        this.stateManager = stateManager;
    }

    @Override
    public void initStatesList(GameContainer gameContainer) {
        this.stateManager.getFrameStageStateStates().forEach(this::addState);
    }

    @Override
    public void preUpdateState(GameContainer gameContainer, int delta) {
        this.updateInputListener(gameContainer.getInput());

        if (this.stateManager.getSelectedFrameStageState().getID() != this.currentStateId) {
            this.enterState(this.currentStateId, this.fadeOutTransition, this.fadeInTransition);
            this.stateManager.setSelectedFrameStageState(this.currentStateId);
        }
    }

    /**
     * Renders the given stage.
     *
     * @param frameStage - the frame stage to be rendered
     */
    public void renderStage(FrameStage frameStage) {
        FrameStageState newState = this.stateManager.getFreeFrameStageState();
        newState.setCurrentStage(frameStage);
        this.currentStateId = newState.getID();
        this.fadeInTransition = new FadeInTransition(Color.black, 300);
        this.fadeOutTransition = new FadeOutTransition(Color.black, 300);
    }

    /**
     * Sets the key listener.
     *
     * @param inputListener - the key listener to be used to handle input.
     */
    public void setInputListener(KeyListener inputListener) {
        this.inputListener = inputListener;
        this.isInputListenerChanged = true;
    }

    private void updateInputListener(Input input) {
        if (this.isInputListenerChanged) {
            input.removeAllKeyListeners();
            if (this.inputListener != null) {
                input.addKeyListener(this.inputListener);
            }
            this.isInputListenerChanged = false;
        }
    }
}
