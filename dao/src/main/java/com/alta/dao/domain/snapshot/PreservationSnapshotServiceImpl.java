package com.alta.dao.domain.snapshot;

import com.alta.dao.configuration.CassandraConnector;
import com.alta.dao.data.preservation.*;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * Provides the service to make manipulations with snapshot.
 */
@Slf4j
public class PreservationSnapshotServiceImpl implements PreservationSnapshotService {

    private final CassandraConnector cassandraConnector;
    private final RestoreSnapshotService restoreSnapshotService;
    private final MakeSnapshotService makeSnapshotService;
    private final Mapper<PreservationSnapshotModel> preservationSnapshotModelMapper;

    /**
     * Initialize new instance of {@link PreservationSnapshotServiceImpl}.
     */
    @Inject
    public PreservationSnapshotServiceImpl(CassandraConnector cassandraConnector,
                                           RestoreSnapshotService restoreSnapshotService,
                                           MakeSnapshotService makeSnapshotService) {
        this.cassandraConnector = cassandraConnector;
        this.restoreSnapshotService = restoreSnapshotService;
        this.makeSnapshotService = makeSnapshotService;

        MappingManager mappingManager = new MappingManager(cassandraConnector.getSession());
        this.preservationSnapshotModelMapper = mappingManager.mapper(PreservationSnapshotModel.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeSnapshot(int preservationId) {
        log.info("Start making snapshot of preservation {}", preservationId);

        Instant start = Instant.now();
        this.makeSnapshotService.makeSnapshot(preservationId);
        Instant finish = Instant.now();

        log.info(
                "Snapshot for preservation {} was created. Total time: {} msec.",
                preservationId, Duration.between(start, finish).toMillis()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreFromSnapshot(int preservationId) {
        log.info("Start restoring preservation snapshot for preservation {}", preservationId);

        Instant start = Instant.now();
        PreservationSnapshotModel snapshot = this.preservationSnapshotModelMapper.get(preservationId);

        if (snapshot == null) {
            throw new RuntimeException("Snapshot for given preservation id " + preservationId + " not found");
        }

        this.restoreSnapshotService.restoreFromSnapshot(snapshot);

        Instant finish = Instant.now();
        log.info(
                "Restoring of preservation {} was completed. Total time: {} msec.",
                preservationId, Duration.between(start, finish).toMillis()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearTemporaryStorage(int preservationId) {
        log.info("Start clearing temporary tables of preservation snapshot for ID {}", preservationId);

        Instant start = Instant.now();
        this.cassandraConnector.getSession().execute(
                new BatchStatement().addAll(this.prepareDeleteStatement(preservationId))
        );

        Instant finish = Instant.now();
        log.info("Temporary tables was cleared. Total time: {} msec.", Duration.between(start, finish).toMillis());
    }

    private List<Statement> prepareDeleteStatement(int preservationId) {
        String deleteQueryTemplate = "DELETE FROM %s WHERE %s=%d";

        String deleteFromPreservation = String.format(
                deleteQueryTemplate, PreservationModel.TABLE_NAME, PreservationModel.ID_FIELD, preservationId
        );
        String deleteFromInteractionPreservation = String.format(
                deleteQueryTemplate,
                InteractionPreservationModel.TABLE_NAME,
                InteractionPreservationModel.PRESERVATION_FIELD, preservationId
        );
        String deleteFromMapPreservation = String.format(
                deleteQueryTemplate, MapPreservationModel.TABLE_NAME, MapPreservationModel.PRESERVATION_FIELD, preservationId
        );
        String deleteFromQuestPreservation = String.format(
                deleteQueryTemplate, QuestPreservationModel.TABLE_NAME, QuestPreservationModel.PRESERVATION_ID_FIELD, preservationId
        );

        return Arrays.asList(
                new SimpleStatement(deleteFromPreservation),
                new SimpleStatement(deleteFromInteractionPreservation),
                new SimpleStatement(deleteFromMapPreservation),
                new SimpleStatement(deleteFromQuestPreservation)
        );
    }
}
