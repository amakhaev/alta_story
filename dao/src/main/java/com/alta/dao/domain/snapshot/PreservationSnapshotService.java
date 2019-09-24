package com.alta.dao.domain.snapshot;

/**
 * Provides the service to make manipulations with snapshot.
 */
public interface PreservationSnapshotService {

    /**
     * Makes the snapshot from current preservation state.
     *
     * @param preservationId - the preservation ID.
     */
    void makeSnapshot(int preservationId);

    /**
     * Restores from snapshot to current preservation state.
     *
     * @param preservationId - the preservation ID.
     */
    void restoreFromSnapshot(int preservationId);

    /**
     * Clears the temporary tables.
     *
     * @param preservationId - the preservation ID.
     */
    void clearTemporaryStorage(int preservationId);

}
