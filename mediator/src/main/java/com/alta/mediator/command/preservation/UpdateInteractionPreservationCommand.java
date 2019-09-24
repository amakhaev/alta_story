package com.alta.mediator.command.preservation;

import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import java.util.UUID;

/**
 * Provides the command that updates the preservation related to interactions.
 */
public class UpdateInteractionPreservationCommand implements Command {

    private final PreservationService preservationService;
    private final int preservationId;
    private final String interactionUuid;
    private final String mapName;
    private final boolean isComplete;

    /**
     * Initialize new instance of {@link UpdateInteractionPreservationCommand}
     */
    @AssistedInject
    public UpdateInteractionPreservationCommand(PreservationService preservationService,
                                                @Assisted int preservationId,
                                                @Assisted("interactionUuid") String interactionUuid,
                                                @Assisted("mapName") String mapName,
                                                @Assisted boolean isComplete) {
        this.preservationService = preservationService;
        this.preservationId = preservationId;
        this.interactionUuid = interactionUuid;
        this.mapName = mapName;
        this.isComplete = isComplete;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.preservationService.upsertInteraction(
                this.preservationId, UUID.fromString(this.interactionUuid), this.mapName, this.isComplete
        );
    }
}
