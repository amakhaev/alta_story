package com.alta.mediator.sceneModule.inputManagement;

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

    private Map<SceneAction, ActionState> actionStates;

    @Setter private ActionEventListener listener;

    /**
     * Initialize new instance of {@link ActionEventListener}
     */
    public ActionProducer() {
        this.actionStates = new HashMap<>();
        Thread daemonThreadProducer = new Thread(this.createRunnableProducer());
        daemonThreadProducer.setDaemon(true);
        daemonThreadProducer.setName("action-producer");
        daemonThreadProducer.start();
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

    private Runnable createRunnableProducer() {
        return () -> {
            while (true) {
                if (this.listener != null) {
                    this.actionStates.entrySet()
                            .stream()
                            .filter(es -> es.getValue() != ActionState.STOPPED)
                            .forEach(es -> this.listener.onActionHandle(es.getKey()));
                }
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        };
    }

    private enum ActionState {
        PRODUCING,
        STOPPED
    }
}
