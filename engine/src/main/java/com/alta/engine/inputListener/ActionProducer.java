package com.alta.engine.inputListener;

import com.alta.engine.asyncTask.AsyncTaskManager;
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
public class ActionProducer {

    private static final long SCHEDULE_INTERVAL = 1000 / 60; // milliseconds

    private Map<SceneAction, ActionState> actionStates;

    @Setter
    private ActionEventListener listener;

    /**
     * Initialize new instance of {@link ActionEventListener}
     */
    @Inject
    public ActionProducer(AsyncTaskManager asyncTaskManager) {
        this.actionStates = new HashMap<>();
        asyncTaskManager.runScheduledTask(this::produce, SCHEDULE_INTERVAL);
    }

    /**
     * Handles start producing of action
     *
     * @param action - the action that should begin producing
     */
    void onActionStartProducing(SceneAction action) {
        this.actionStates.put(action, ActionState.PRODUCING);
    }

    /**
     * Handles stop producing of action
     *
     * @param action - the action that should stop producing
     */
    void onActionStopProducing(SceneAction action) {
        this.actionStates.put(action, ActionState.STOPPED);
    }

    private void produce() {
        if (this.listener != null) {
            this.actionStates.entrySet()
                    .stream()
                    .filter(es -> es.getValue() != ActionState.STOPPED)
                    .forEach(es -> this.listener.onActionHandle(es.getKey()));
        }
    }

    private enum ActionState {
        PRODUCING,
        STOPPED
    }
}
