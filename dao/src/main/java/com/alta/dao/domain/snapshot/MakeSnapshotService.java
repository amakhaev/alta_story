package com.alta.dao.domain.snapshot;

import com.alta.dao.configuration.CassandraConnector;
import com.alta.dao.data.preservation.*;
import com.alta.dao.data.preservation.udt.InteractionUdt;
import com.alta.dao.data.preservation.udt.MapUdt;
import com.alta.dao.data.preservation.udt.QuestUdt;
import com.alta.dao.domain.preservation.InteractionPreservationAccessor;
import com.alta.dao.domain.preservation.MapPreservationAccessor;
import com.alta.dao.domain.preservation.QuestPreservationAccessor;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the service to make data to snapshot.
 */
public class MakeSnapshotService {

    private final Mapper<PreservationModel> preservationModelMapper;
    private final Mapper<PreservationSnapshotModel> preservationSnapshotModelMapper;

    private final InteractionPreservationAccessor interactionPreservationAccessor;
    private final MapPreservationAccessor mapPreservationAccessor;
    private final QuestPreservationAccessor questPreservationAccessor;

    /**
     * Initialize new instance {@link MakeSnapshotService}.
     *
     * @param cassandraConnector - the {@link CassandraConnector} instance.
     */
    @Inject
    public MakeSnapshotService(CassandraConnector cassandraConnector) {
        MappingManager mappingManager = new MappingManager(cassandraConnector.getSession());
        this.preservationModelMapper = mappingManager.mapper(PreservationModel.class);
        this.preservationSnapshotModelMapper = mappingManager.mapper(PreservationSnapshotModel.class);

        this.interactionPreservationAccessor = mappingManager.createAccessor(InteractionPreservationAccessor.class);
        this.mapPreservationAccessor = mappingManager.createAccessor(MapPreservationAccessor.class);
        this.questPreservationAccessor = mappingManager.createAccessor(QuestPreservationAccessor.class);
    }

    /**
     * Makes snapshot from current preservation state.
     *
     * @param preservationId - the preservation id to be saved as snapshot.
     */
    void makeSnapshot(int preservationId) {
        PreservationModel preservationModel = this.preservationModelMapper.get(preservationId);

        PreservationSnapshotModel snapshot = PreservationSnapshotModel.builder()
                .id(preservationId)
                .actingCharacter(preservationModel.getActingCharacter())
                .chapterIndicator(preservationModel.getChapterIndicator())
                .interactions(this.getInteractionPreservation(preservationId))
                .maps(this.getMapPreservation(preservationId))
                .quests(this.getQuestPreservation(preservationId))
                .build();

        this.preservationSnapshotModelMapper.save(snapshot);
    }

    private List<InteractionUdt> getInteractionPreservation(int preservationId) {
        List<InteractionPreservationModel> interactions = this.interactionPreservationAccessor.findAllByPreservationId(
                preservationId
        ).all();

        if (interactions == null || interactions.isEmpty()) {
            return Collections.emptyList();
        }

        return interactions.stream()
                .map(interactionModel -> InteractionUdt.builder()
                        .interactionUuid(interactionModel.getInteractionUuid())
                        .mapName(interactionModel.getMapName())
                        .isCompleted(interactionModel.isCompleted())
                        .build()
                ).collect(Collectors.toList());
    }

    private List<MapUdt> getMapPreservation(int preservationId) {
        List<MapPreservationModel> maps = this.mapPreservationAccessor.findAllByPreservationId(
                preservationId
        ).all();

        if (maps == null || maps.isEmpty()) {
            return Collections.emptyList();
        }

        return maps.stream()
                .map(mapModel -> MapUdt.builder()
                        .participantUuid(mapModel.getParticipantUuid())
                        .mapName(mapModel.getMapName())
                        .isVisible(mapModel.isVisible())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private List<QuestUdt> getQuestPreservation(int preservationId) {
        List<QuestPreservationModel> quests = this.questPreservationAccessor.findAllByPreservationId(
                preservationId
        ).all();

        if (quests == null || quests.isEmpty()) {
            return Collections.emptyList();
        }

        return quests.stream()
                .map(questModel -> QuestUdt.builder()
                        .questName(questModel.getQuestName())
                        .currentStepNumber(questModel.getCurrentStepNumber())
                        .isCompleted(questModel.isCompleted())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
