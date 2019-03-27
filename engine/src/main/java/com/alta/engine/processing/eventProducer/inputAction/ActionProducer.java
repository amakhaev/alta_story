package com.alta.engine.processing.eventProducer.inputAction;

import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.processing.listener.sceneInput.SceneAction;
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
    private static final String SCHEDULED_TASK_NAME = "action-event-produce";


    private Map<SceneAction, ActionState> actionStates;

    @Setter
    private ActionEventHandler listener;

    /**
     * Initialize new instance of {@link ActionEventHandler}
     */
    @Inject
    public ActionProducer(AsyncTaskManager asyncTaskManager) {
        this.actionStates = new HashMap<>();
        asyncTaskManager.runScheduledTask(this::produce, SCHEDULED_TASK_NAME, SCHEDULE_INTERVAL);
    }

    /**
     * Handles start producing of action
     *
     * @param action - the action that should begin producing
     */
    public void onActionStartProducing(SceneAction action) {
        this.actionStates.put(action, ActionState.PRODUCING);
    }

    /**
     * Handles stop producing of action
     *
     * @param action - the action that should stop producing
     */
    public void onActionStopProducing(SceneAction action) {
        this.actionStates.put(action, ActionState.STOPPED);
    }

    private void produce() {
        if (this.listener != null) {
            this.actionStates.entrySet()
                    .stream()
                    .filter(es -> es.getValue() != ActionState.STOPPED)
                    .forEach(es -> this.listener.handle(es.getKey()));
        }
    }

    private enum ActionState {
        PRODUCING,
        STOPPED
    }
}
