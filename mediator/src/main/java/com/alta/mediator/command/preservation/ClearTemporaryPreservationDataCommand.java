package com.alta.mediator.command.preservation;

import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.AssistedInject;

import javax.inject.Named;

/**
 * Clears all temporary model for preservation.
 */
public class ClearTemporaryPreservationDataCommand implements Command {

    private final PreservationService preservationService;
    private final Long currentPreservationId;

    /**
     * Initialize new instance of {@link ClearTemporaryPreservationDataCommand}.
     */
    @AssistedInject
    public ClearTemporaryPreservationDataCommand(PreservationService preservationService,
                                                 @Named("currentPreservationId") Long currentPreservationId) {
        this.preservationService = preservationService;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.preservationService.clearTemporaryDataFromPreservation(this.currentPreservationId);
    }
}
