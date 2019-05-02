package com.alta.mediator.command.preservation;

import com.alta.dao.domain.preservation.TemporaryDataPreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.AssistedInject;

import javax.inject.Named;

/**
 * Clears all temporary data for preservation.
 */
public class ClearTemporaryPreservationDataCommand implements Command {

    private final TemporaryDataPreservationService temporaryDataPreservationService;
    private final Long currentPreservationId;

    /**
     * Initialize new instance of {@link ClearTemporaryPreservationDataCommand}.
     */
    @AssistedInject
    public ClearTemporaryPreservationDataCommand(TemporaryDataPreservationService temporaryDataPreservationService,
                                                 @Named("currentPreservationId") Long currentPreservationId) {
        this.temporaryDataPreservationService = temporaryDataPreservationService;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.temporaryDataPreservationService.clearTemporaryDataFromPreservation(this.currentPreservationId);
    }
}
