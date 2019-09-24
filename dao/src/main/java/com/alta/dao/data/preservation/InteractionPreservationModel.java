package com.alta.dao.data.preservation;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.UUID;

/**
 * Represent preservation of characters in database.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = InteractionPreservationModel.TABLE_NAME)
public class InteractionPreservationModel {

    public static final String TABLE_NAME = "interaction_preservations";

    public static final String PRESERVATION_FIELD = "preservation_id";
    public static final String INTERACTION_UUID_FIELD = "interaction_uuid";
    public static final String MAP_NAME_FIELD = "map_name";
    public static final String COMPLETED_FIELD = "completed";

    @PartitionKey
    @Column(name = PRESERVATION_FIELD)
    @SerializedName(PRESERVATION_FIELD)
    private int preservationId;

    @SerializedName(INTERACTION_UUID_FIELD)
    @Column(name = INTERACTION_UUID_FIELD)
    private UUID interactionUuid;

    @SerializedName(MAP_NAME_FIELD)
    @Column(name = MAP_NAME_FIELD)
    private String mapName;

    @SerializedName(COMPLETED_FIELD)
    @Column(name = COMPLETED_FIELD)
    private boolean isCompleted;
}
