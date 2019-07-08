package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.domain.preservation.interaction.InteractionPreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Provides the command that updates the preservation related to interactions.
 */
public class UpdateInteractionPreservationCommand implements Command {

    private final InteractionPreservationService interactionPreservationService;
    private final InteractionPreservationModel interactionPreservation;

    /**
     * Initialize new instance of {@link UpdateInteractionPreservationCommand}
     */
    @AssistedInject
    public UpdateInteractionPreservationCommand(InteractionPreservationService interactionPreservationService,
                                                @Assisted InteractionPreservationModel interactionPreservation) {
        this.interactionPreservationService = interactionPreservationService;
        this.interactionPreservation = interactionPreservation;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.interactionPreservationService.upsertTemporaryInteractionPreservation(this.interactionPreservation);
    }
}
