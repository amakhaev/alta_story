package com.alta.dao.data.preservation;

import com.datastax.driver.mapping.annotations.*;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Represent preservation of map in database.
 */
@Getter
@Setter
@Builder
@Table(name = MapPreservationModel.TABLE_NAME)
public class MapPreservationModel {

    public static final String TABLE_NAME = "map_preservations";

    public static final String PRESERVATION_FIELD = "preservation_id";
    public static final String PARTICIPANT_UUID_FIELD = "participant_uuid";
    public static final String MAP_NAME_FIELD = "map_name";
    public static final String VISIBLE_FIELD = "visible";

    @PartitionKey
    @Column(name = PRESERVATION_FIELD)
    @SerializedName(PRESERVATION_FIELD)
    private int preservationId;

    @SerializedName(PARTICIPANT_UUID_FIELD)
    @Column(name = PARTICIPANT_UUID_FIELD)
    private UUID participantUuid;

    @SerializedName(MAP_NAME_FIELD)
    @Column(name = MAP_NAME_FIELD)
    private String mapName;

    @SerializedName(VISIBLE_FIELD)
    @Column(name = VISIBLE_FIELD)
    private boolean isVisible;

}
