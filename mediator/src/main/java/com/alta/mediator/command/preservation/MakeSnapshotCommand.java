package com.alta.mediator.command.preservation;

import com.alta.dao.domain.snapshot.PreservationSnapshotService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Provides the command to make snapshot of preservation.
 */
public class MakeSnapshotCommand implements Command {

    private final int preservationId;
    private final PreservationSnapshotService preservationSnapshotService;

    /**
     * Initialize new instance of {@link MakeSnapshotCommand}.
     */
    @AssistedInject
    public MakeSnapshotCommand(@Assisted int preservationId, PreservationSnapshotService preservationSnapshotService) {
        this.preservationId = preservationId;
        this.preservationSnapshotService = preservationSnapshotService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        this.preservationSnapshotService.makeSnapshot(this.preservationId);
    }
}
