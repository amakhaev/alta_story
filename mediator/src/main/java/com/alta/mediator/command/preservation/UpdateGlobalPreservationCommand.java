package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.GlobalPreservationModel;
import com.alta.dao.domain.preservation.global.GlobalPreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.extern.slf4j.Slf4j;

/**
 * Update the global preservation command.
 */
@Slf4j
public class UpdateGlobalPreservationCommand implements Command {

    private final GlobalPreservationService globalPreservationService;
    private final GlobalPreservationModel globalPreservationModel;

    /**
     * Initialize new instance of {@link UpdateGlobalPreservationCommand}.
     * @param globalPreservationService - the {@link GlobalPreservationService} instance.
     * @param globalPreservationModel   - the preservation to be updated.
     */
    @AssistedInject
    public UpdateGlobalPreservationCommand(GlobalPreservationService globalPreservationService,
                                           @Assisted GlobalPreservationModel globalPreservationModel) {
        this.globalPreservationService = globalPreservationService;
        this.globalPreservationModel = globalPreservationModel;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.globalPreservationService.upsertTemporaryQuestPreservation(this.globalPreservationModel);
    }
}
