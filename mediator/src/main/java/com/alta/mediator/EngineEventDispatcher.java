package com.alta.mediator;

import com.alta.engine.eventProducer.EngineEvent;
import com.alta.engine.eventProducer.eventPayload.JumpingEventPayload;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.CommandFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the dispatcher of events from {@link com.alta.engine.Engine}
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EngineEventDispatcher {

    private final CommandFactory commandFactory;
    private final CommandExecutor commandExecutor;

    /**
     * Dispatches the event from Engine.
     *
     * @param event - the event to be handled.
     */
    public void dispatch(@NonNull EngineEvent event) {
        log.info("Have got an event from Engine. Type: {}", event.getType());

        try {
            switch (event.getType()) {
                case JUMPING:
                    this.executeRenderCommand(event.tryToCastPayload(JumpingEventPayload.class));
                    break;
                default:
                    log.error("Unknown type of payload {}", event.getType());
            }
        } catch (ClassCastException e) {
            log.error(e.getMessage());
        }
    }

    private void executeRenderCommand(JumpingEventPayload payload) {
        this.commandExecutor.executeCommand(
                this.commandFactory.createRenderFrameStageByParametersCommand(
                        payload.getMapName(),
                        "person1",
                        payload.getMapStartCoordinates()
                )
        );
    }
}
