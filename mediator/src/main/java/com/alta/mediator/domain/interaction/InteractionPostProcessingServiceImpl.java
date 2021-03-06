package com.alta.mediator.domain.interaction;

import com.alta.dao.data.interaction.InteractionDataModel;
import com.alta.dao.data.interaction.postProcessing.UpdateFacilityVisibilityPostProcessModel;
import com.alta.dao.domain.interaction.InteractionService;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides the service that handles the post processors related to interaction.
 */
@Slf4j
public class InteractionPostProcessingServiceImpl implements InteractionPostProcessingService {

    private final Long currentPreservationId;
    private final InteractionService interactionService;
    private final CommandExecutor commandExecutor;
    private final PreservationCommandFactory preservationCommandFactory;

    /**
     * Initialize new instance of {@link InteractionPostProcessingServiceImpl}.
     */
    @Inject
    public InteractionPostProcessingServiceImpl(InteractionService interactionService,
                                                @Named("currentPreservationId") Long currentPreservationId,
                                                CommandExecutor commandExecutor,
                                                PreservationCommandFactory preservationCommandFactory) {
        this.interactionService = interactionService;
        this.currentPreservationId = currentPreservationId;
        this.commandExecutor = commandExecutor;
        this.preservationCommandFactory = preservationCommandFactory;
    }

    /**
     * Executes the post processors for interaction.
     *
     * @param uuid    - the uuid of interaction.
     * @param mapName - the name of map with interaction.
     */
    @Override
    public void executeInteractionPostProcessing(@NonNull String uuid, @NonNull String mapName) {
        InteractionDataModel interaction = this.interactionService.getInteraction(mapName, uuid);
        if (interaction == null) {
            log.warn("Effect for given map '{}' and uuid {} not found", mapName, uuid);
            return;
        }

        if (interaction.getPostProcessors() == null || interaction.getPostProcessors().isEmpty()) {
            log.debug("Post processors for interaction for given map '{}' and uuid {} not found", mapName, uuid);
            return;
        }

        List<Command> commands = interaction.getPostProcessors()
                .stream()
                .map(postProcessor -> {
                    switch (postProcessor.getType()) {
                        case UPDATE_FACILITY_VISIBILITY:
                            return this.getUpdateFacilityVisibilityCommand(
                                    (UpdateFacilityVisibilityPostProcessModel)postProcessor, mapName
                            );
                        default:
                            log.error("Unknown type of post processor type {}", postProcessor.getType());
                            return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        this.commandExecutor.executeCommands(commands);
    }

    private Command getUpdateFacilityVisibilityCommand(UpdateFacilityVisibilityPostProcessModel postProcessModel,
                                                       String mapName) {
        return this.preservationCommandFactory.createUpdateMapPreservationCommand(
                this.currentPreservationId.intValue(), postProcessModel.getFacilityUuid(), mapName, postProcessModel.isValue()
        );
    }
}
