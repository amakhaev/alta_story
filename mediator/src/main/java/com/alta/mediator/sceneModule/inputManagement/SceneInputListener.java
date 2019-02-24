package com.alta.mediator.sceneModule.inputManagement;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the input listener of scene
 */
@Singleton
public class SceneInputListener implements KeyListener {

    private static final Map<Integer, SceneAction> INPUT_TO_ACTIONS = new HashMap<Integer, SceneAction>() {{
        put(Input.KEY_RIGHT, SceneAction.MOVE_RIGHT);
        put(Input.KEY_LEFT, SceneAction.MOVE_LEFT);
        put(Input.KEY_UP, SceneAction.MOVE_UP);
        put(Input.KEY_DOWN, SceneAction.MOVE_DOWN);
    }};

    private final ActionProducer actionProducer;

    /**
     * Initialize new instance of {@link SceneInputListener}
     */
    @Inject
    public SceneInputListener(ActionProducer producer) {
        this.actionProducer = producer;
    }

    @Override
    public void keyPressed(int i, char c) {
        if (INPUT_TO_ACTIONS.containsKey(i)) {
            this.actionProducer.onActionStartProducing(INPUT_TO_ACTIONS.get(i));
        }
    }

    @Override
    public void keyReleased(int i, char c) {
        if (INPUT_TO_ACTIONS.containsKey(i)) {
            this.actionProducer.onActionStopProducing(INPUT_TO_ACTIONS.get(i));
        }
    }

    @Override
    public void setInput(Input input) {

    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {
    }

    @Override
    public void inputStarted() {
    }
}
