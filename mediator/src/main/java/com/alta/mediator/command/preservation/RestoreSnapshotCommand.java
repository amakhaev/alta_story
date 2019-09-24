package com.alta.mediator.command.preservation;

import com.alta.dao.domain.snapshot.PreservationSnapshotService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Provides the command to restore snapshot from preservation.
 */
public class RestoreSnapshotCommand implements Command {

    private final int preservationId;
    private final PreservationSnapshotService preservationSnapshotService;

    /**
     * Initialize new instance of {@link RestoreSnapshotCommand}.
     */
    @AssistedInject
    public RestoreSnapshotCommand(@Assisted int preservationId, PreservationSnapshotService preservationSnapshotService) {
        this.preservationId = preservationId;
        this.preservationSnapshotService = preservationSnapshotService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        this.preservationSnapshotService.clearTemporaryStorage(this.preservationId);
        this.preservationSnapshotService.restoreFromSnapshot(this.preservationId);
    }
}
