package com.alta.dao.domain.snapshot;

import com.alta.dao.configuration.CassandraConnector;
import com.alta.dao.data.preservation.*;
import com.alta.dao.data.preservation.udt.ActingCharacterUdt;
import com.alta.dao.data.preservation.udt.InteractionUdt;
import com.alta.dao.data.preservation.udt.MapUdt;
import com.alta.dao.data.preservation.udt.QuestUdt;
import com.alta.utils.JsonParser;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the service to restore data from snapshot.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class RestoreSnapshotService {

    private final CassandraConnector cassandraConnector;

    /**
     * Restores from snapshot to current preservation state.
     *
     * @param snapshot - the preservation snapshot to be restored.
     */
    void restoreFromSnapshot(PreservationSnapshotModel snapshot) {
        List<Statement> statements = new ArrayList<>();
        statements.add(
                this.preparePreservationStatement(
                        snapshot.getId(), snapshot.getActingCharacter(), snapshot.getChapterIndicator()
                )
        );
        statements.addAll(this.prepareInteractionStatement(snapshot.getId(), snapshot.getInteractions()));
        statements.addAll(this.prepareMapStatement(snapshot.getId(), snapshot.getMaps()));
        statements.addAll(this.prepareQuestStatement(snapshot.getId(), snapshot.getQuests()));

        this.cassandraConnector.getSession().execute(new BatchStatement().addAll(statements));
    }

    private Statement preparePreservationStatement(int preservationId, ActingCharacterUdt actingCharacter, int chapterIndicator) {
        PreservationModel model = PreservationModel.builder()
                .id(preservationId)
                .actingCharacter(actingCharacter)
                .chapterIndicator(chapterIndicator)
                .build();

        return this.cassandraConnector.getSession().prepare(
                "INSERT INTO " + PreservationModel.TABLE_NAME + " JSON :preservationJson"
        ).bind(JsonParser.toJson(model));
    }

    private List<Statement> prepareInteractionStatement(int preservationId, List<InteractionUdt> interactions) {
        if (interactions == null || interactions.isEmpty()) {
            return Collections.emptyList();
        }

        return interactions.stream()
                .map(interaction -> {
                    InteractionPreservationModel model = InteractionPreservationModel.builder()
                            .preservationId(preservationId)
                            .interactionUuid(interaction.getInteractionUuid())
                            .mapName(interaction.getMapName())
                            .isCompleted(interaction.isCompleted())
                            .build();

                    return this.cassandraConnector.getSession().prepare(
                            "INSERT INTO " + InteractionPreservationModel.TABLE_NAME + " JSON :interactionJson"
                    ).bind(JsonParser.toJson(model));
                })
                .collect(Collectors.toList());
    }

    private List<Statement> prepareMapStatement(int preservationId, List<MapUdt> maps) {
        if (maps == null || maps.isEmpty()) {
            return Collections.emptyList();
        }

        return maps.stream()
                .map(map -> {
                    MapPreservationModel model = MapPreservationModel.builder()
                            .preservationId(preservationId)
                            .participantUuid(map.getParticipantUuid())
                            .mapName(map.getMapName())
                            .isVisible(map.isVisible())
                            .build();

                    return this.cassandraConnector.getSession().prepare(
                            "INSERT INTO " + MapPreservationModel.TABLE_NAME + " JSON :mapJson"
                    ).bind(JsonParser.toJson(model));
                })
                .collect(Collectors.toList());
    }

    private List<Statement> prepareQuestStatement(int preservationId, List<QuestUdt> quests) {
        if (quests == null || quests.isEmpty()) {
            return Collections.emptyList();
        }

        return quests.stream()
                .map(quest -> {
                    QuestPreservationModel model = QuestPreservationModel.builder()
                            .preservationId(preservationId)
                            .questName(quest.getQuestName())
                            .currentStepNumber(quest.getCurrentStepNumber())
                            .isCompleted(quest.isCompleted())
                            .build();

                    return this.cassandraConnector.getSession().prepare(
                            "INSERT INTO " + QuestPreservationModel.TABLE_NAME + " JSON :mapJson"
                    ).bind(JsonParser.toJson(model));
                })
                .collect(Collectors.toList());
    }
}
