package com.alta.engine.presenter.sceneProxy.sceneInput;

import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the producer of actions.
 */
@Singleton
@Slf4j
public class KeyActionProducer {

    private static final long SCHEDULE_INTERVAL = 1000 / 60; // milliseconds
    private static final String SCHEDULED_TASK_NAME = "action-event-produce";


    private Map<SceneAction, ActionState> actionStates;

    @Setter
    private ActionEventListener listener;

    /**
     * Initialize new instance of {@link ActionEventListener}
     */
    @Inject
    public KeyActionProducer(AsyncTaskManager asyncTaskManager) {
        this.actionStates = new HashMap<>();
        asyncTaskManager.runScheduledTask(this::produce, SCHEDULED_TASK_NAME, SCHEDULE_INTERVAL);
    }

    /**
     * Handles the starting of actions.
     *
     * @param action - the action that will be started.
     */
    void onActionStarted(SceneAction action) {
        this.actionStates.put(action, ActionState.PRODUCING);
    }

    /**
     * Handles stop producing of action
     *
     * @param action - the action that should stop producing
     */
    void onActionReleased(SceneAction action) {
        this.actionStates.put(action, ActionState.STOPPED);
        if (this.listener != null) {
            this.listener.onActionReleased(action);
        }
    }

    private void produce() {
        if (this.listener != null) {
            this.actionStates.entrySet()
                    .stream()
                    .filter(es -> es.getValue() != ActionState.STOPPED)
                    .forEach(es -> this.listener.onPerformAction(es.getKey()));
        }
    }

    private enum ActionState {
        PRODUCING,
        STOPPED
    }
}
