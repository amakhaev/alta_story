package com.alta.mediator.command.preservation;

import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import java.util.UUID;

/**
 * Provides the command that updates the map preservation.
 */
public class UpdateMapPreservationCommand implements Command {

    private final PreservationService preservationService;
    private final int preservationId;
    private final String participantUuid;
    private final String mapName;
    private final boolean isVisible;

    /**
     * Initialize ew instance of {@link UpdateMapPreservationCommand}.
     */
    @AssistedInject
    public UpdateMapPreservationCommand(PreservationService preservationService,
                                        @Assisted int preservationId,
                                        @Assisted("participantUuid") String participantUuid,
                                        @Assisted("mapName") String mapName,
                                        @Assisted boolean isVisible) {
        this.preservationService = preservationService;
        this.preservationId = preservationId;
        this.participantUuid = participantUuid;
        this.mapName = mapName;
        this.isVisible = isVisible;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.preservationService.upsertMap(
                this.preservationId, UUID.fromString(this.participantUuid), this.mapName, this.isVisible
        );
    }
}
