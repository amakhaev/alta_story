package com.alta.mediator;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.domain.preservation.TemporaryDataPreservationService;
import com.alta.engine.eventProducer.EngineEvent;
import com.alta.engine.eventProducer.eventPayload.InteractionCompletedEventPayload;
import com.alta.engine.eventProducer.eventPayload.JumpingEventPayload;
import com.alta.engine.eventProducer.eventPayload.SaveStateEventPayload;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.frameStage.FrameStageCommandFactory;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.mediator.domain.interaction.InteractionPostProcessingService;
import com.alta.utils.ExecutorServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.concurrent.ExecutorService;

/**
 * Provides the dispatcher of events from {@link com.alta.engine.Engine}
 */
@Slf4j
@Singleton
public class EngineEventDispatcher {

    private final FrameStageCommandFactory frameStageCommandFactory;
    private final PreservationCommandFactory preservationCommandFactory;
    private final CommandExecutor commandExecutor;
    private final TemporaryDataPreservationService temporaryDataPreservationService;
    private final InteractionPostProcessingService interactionPostProcessingService;
    private final Long currentPreservationId;
    private final ExecutorService eventThreadExecutor;

    /**
     * Initialize new instance of {@link EngineEventDispatcher}.
     */
    @Inject
    public EngineEventDispatcher(FrameStageCommandFactory frameStageCommandFactory,
                                 PreservationCommandFactory preservationCommandFactory,
                                 CommandExecutor commandExecutor,
                                 TemporaryDataPreservationService temporaryDataPreservationService,
                                 InteractionPostProcessingService interactionPostProcessingService,
                                 @Named("currentPreservationId") Long currentPreservationId) {
        this.frameStageCommandFactory = frameStageCommandFactory;
        this.preservationCommandFactory = preservationCommandFactory;
        this.commandExecutor = commandExecutor;
        this.temporaryDataPreservationService = temporaryDataPreservationService;
        this.interactionPostProcessingService = interactionPostProcessingService;
        this.currentPreservationId = currentPreservationId;
        this.eventThreadExecutor = ExecutorServiceFactory.create(3, "engine_event_dispatcher");
    }

    /**
     * Dispatches the event from Engine.
     *
     * @param event - the event to be handled.
     */
    public void dispatch(@NonNull EngineEvent event) {
        this.eventThreadExecutor.execute(() -> {
            log.info("Have got an event from Engine. Type: {}", event.getType());

            try {
                switch (event.getType()) {
                    case JUMPING:
                        this.executeRenderCommand(event.tryToCastPayload(JumpingEventPayload.class));
                        break;
                    case SAVE_STATE:
                        this.executeSaving(event.tryToCastPayload(SaveStateEventPayload.class));
                        break;
                    case INTERACTION_COMPLETED:
                        log.error("Should not be here");
                        break;
                    default:
                        log.error("Unknown type of payload {}", event.getType());
                }
            } catch (ClassCastException e) {
                log.error(e.getMessage());
            }
        });
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





    private void executeSaving(@NonNull SaveStateEventPayload payload) {
        CharacterPreservationModel characterPreservationModel = CharacterPreservationModel.builder()
                .id(this.currentPreservationId)
                .focusX(payload.getMapCoordinates().x)
                .focusY(payload.getMapCoordinates().y)
                .skin(payload.getSkinName())
                .mapName(payload.getMapName())
                .build();

        Command command = this.preservationCommandFactory.createSavePreservationCommand(characterPreservationModel);
        this.commandExecutor.executeCommand(command);
        log.info("Saving of preservation completed to preservation {}", this.currentPreservationId);
    }
}
