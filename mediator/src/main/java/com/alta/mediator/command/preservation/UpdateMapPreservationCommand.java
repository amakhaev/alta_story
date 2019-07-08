package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.MapPreservationModel;
import com.alta.dao.domain.preservation.map.MapPreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Provides the command that updates the map preservation.
 */
public class UpdateMapPreservationCommand implements Command {

    private final MapPreservationModel mapPreservationModel;
    private final MapPreservationService mapPreservationService;

    /**
     * Initialize ew instance of {@link UpdateMapPreservationCommand}.
     */
    @AssistedInject
    public UpdateMapPreservationCommand(@Assisted MapPreservationModel mapPreservationModel,
                                        MapPreservationService mapPreservationService) {
        this.mapPreservationModel = mapPreservationModel;
        this.mapPreservationService = mapPreservationService;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.mapPreservationService.upsertTemporaryMapPreservation(this.mapPreservationModel);
    }
}
