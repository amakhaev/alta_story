package com.alta.mediator.command.interaction;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.domain.preservation.TemporaryDataPreservationService;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.mediator.domain.interaction.InteractionPostProcessingService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import javax.inject.Named;

/**
 * Provides the command that mark interaction as completed.
 */
public class CompleteInteractionCommand implements Command {

    private final PreservationCommandFactory preservationCommandFactory;
    private final CommandExecutor commandExecutor;
    private final InteractionPostProcessingService interactionPostProcessingService;
    private final TemporaryDataPreservationService temporaryDataPreservationService;
    private final Long currentPreservationId;
    private final String interactionUuid;
    private final String relatedMapName;

    /**
     * Initialize new instance of {@link CompleteInteractionCommand}.
     * @param preservationCommandFactory        - the {@link PreservationCommandFactory} instance.
     * @param commandExecutor                   - the {@link CommandExecutor} instance.
     * @param interactionPostProcessingService  - the {@link InteractionPostProcessingService} instance.
     * @param temporaryDataPreservationService  - the {@link TemporaryDataPreservationService} instance.
     * @param currentPreservationId             - the Id of current preservation.
     * @param interactionUuid                   - the uuid of interaction that was completed.
     * @param relatedMapName                    - the name of map where interaction was executing.
     */
    @AssistedInject
    public CompleteInteractionCommand(PreservationCommandFactory preservationCommandFactory,
                                      CommandExecutor commandExecutor,
                                      InteractionPostProcessingService interactionPostProcessingService,
                                      TemporaryDataPreservationService temporaryDataPreservationService,
                                      @Named("currentPreservationId") Long currentPreservationId,
                                      @Assisted("interactionUuid") String interactionUuid,
                                      @Assisted("relatedMapName") String relatedMapName) {
        this.preservationCommandFactory = preservationCommandFactory;
        this.commandExecutor = commandExecutor;
        this.interactionPostProcessingService = interactionPostProcessingService;
        this.temporaryDataPreservationService = temporaryDataPreservationService;
        this.currentPreservationId = currentPreservationId;
        this.interactionUuid = interactionUuid;
        this.relatedMapName = relatedMapName;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.executeUpdateInteractionPreservationCommand();
        this.interactionPostProcessingService.executeInteractionPostProcessing(this.interactionUuid, this.relatedMapName);
    }

    private void executeUpdateInteractionPreservationCommand() {
        InteractionPreservationModel interactionPreservationToUpdate;
        interactionPreservationToUpdate = this.temporaryDataPreservationService.getTemporaryInteractionPreservation(
                this.currentPreservationId, this.interactionUuid
        );

        if (interactionPreservationToUpdate == null) {
            interactionPreservationToUpdate = InteractionPreservationModel
                    .builder()
                    .preservationId(this.currentPreservationId)
                    .uuid(this.interactionUuid)
                    .isComplete(true)
                    .isTemporary(true)
                    .mapName(this.relatedMapName)
                    .build();
        } else {
            interactionPreservationToUpdate.setCompleted(true);
        }

        Command command = this.preservationCommandFactory.createUpdateInteractionPreservationCommand(
                interactionPreservationToUpdate
        );

        this.commandExecutor.executeCommand(command);
    }
}
