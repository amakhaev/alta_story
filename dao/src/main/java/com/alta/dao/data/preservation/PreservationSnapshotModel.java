package com.alta.dao.data.preservation;

import com.alta.dao.data.preservation.udt.ActingCharacterUdt;
import com.alta.dao.data.preservation.udt.InteractionUdt;
import com.alta.dao.data.preservation.udt.MapUdt;
import com.alta.dao.data.preservation.udt.QuestUdt;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.*;

import java.util.List;

/**
 * Represent preservation of preservation in database.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "preservation_snapshots")
public class PreservationSnapshotModel {

    @PartitionKey
    private int id;

    @Column(name = "chapter_indicator")
    private int chapterIndicator;

    @Column(name = "acting_character")
    private ActingCharacterUdt actingCharacter;

    @Frozen
    @Column(name = "interactions")
    private List<InteractionUdt> interactions;

    @Frozen
    @Column(name = "maps")
    private List<MapUdt> maps;

    @Frozen
    @Column(name = "quests")
    private List<QuestUdt> quests;
}
