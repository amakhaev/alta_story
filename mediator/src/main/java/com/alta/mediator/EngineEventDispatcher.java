package com.alta.mediator;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.engine.eventProducer.EngineEvent;
import com.alta.engine.eventProducer.eventPayload.JumpingEventPayload;
import com.alta.engine.eventProducer.eventPayload.SaveStateEventPayload;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.frameStage.FrameStageCommandFactory;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
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

    private final FrameStageCommandFactory frameStageCommandFactory;
    private final PreservationCommandFactory preservationCommandFactory;
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
                case SAVE_STATE:
                    this.executeUpdateCharacterPreservationCommand(event.tryToCastPayload(SaveStateEventPayload.class));
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
                this.frameStageCommandFactory.createRenderFrameStageByParametersCommand(
                        payload.getMapName(),
                        "person1",
                        payload.getMapStartCoordinates()
                )
        );
    }

    private void executeUpdateCharacterPreservationCommand(@NonNull SaveStateEventPayload payload) {
        Command command = this.preservationCommandFactory.createUpdateCharacterPreservationCommand(
                CharacterPreservationModel.builder()
                        .id(1L)
                        .focusX(payload.getMapCoordinates().x)
                        .focusY(payload.getMapCoordinates().y)
                        .skin(payload.getSkinName())
                        .mapName(payload.getMapName())
                        .build()
        );

        this.commandExecutor.executeCommand(command);
        log.info("Updating the character preservation with id {} completed.", 1L);
    }
}
