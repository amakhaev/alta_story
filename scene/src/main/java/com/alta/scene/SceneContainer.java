package com.alta.scene;

import com.alta.scene.configuration.SceneConfig;
import com.alta.scene.core.states.FrameStageState;
import com.alta.scene.core.states.StateManager;
import com.alta.scene.entities.FrameStage;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.state.StateBasedGame;

import javax.inject.Named;

@Singleton
@Slf4j
public class SceneContainer extends StateBasedGame {

    private final StateManager stateManager;

    private KeyListener inputListener;
    private boolean isInputListenerChanged;
    private int currentStateId;

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
        if (this.isInputListenerChanged) {
            gameContainer.getInput().removeAllKeyListeners();
            if (this.inputListener != null) {
                gameContainer.getInput().addKeyListener(this.inputListener);
            }
            this.isInputListenerChanged = false;
        }

        if (this.getCurrentStateID() != this.currentStateId) {
            this.enterState(this.currentStateId);
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
}
