package com.alta.dao.data.preservation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that stored changes on map.
 */
@Getter
@DatabaseTable(tableName = "map_preservations")
public class MapPreservationModel {

    @Builder
    private static MapPreservationModel create(long preservationId,
                                               String participantUuid,
                                               String mapName,
                                               boolean isVisible,
                                               boolean isTemporary) {
        MapPreservationModel mapPreservationModel = new MapPreservationModel();
        mapPreservationModel.preservationId = preservationId;
        mapPreservationModel.mapName = mapName;
        mapPreservationModel.participantUuid = participantUuid;
        mapPreservationModel.isVisible = isVisible;
        mapPreservationModel.isTemporary = isTemporary;

        return mapPreservationModel;
    }

    /**
     * Provides the ID field name.
     */
    public static final String ID_FIELD = "id";

    /**
     * Provides the preservation id field name.
     */
    public static final String PRESERVATION_ID_FIELD = "preservation_id";

    /**
     * Provides the uuid field name.
     */
    public static final String UUID_FIELD = "uuid";

    /**
     * Provides the map name field name.
     */
    public static final String MAP_NAME_FIELD = "map_name";

    /**
     * Provides the is visible field name.
     */
    public static final String IS_VISIBLE_FIELD = "is_visible";

    /**
     * Provides the is temporary field name.
     */
    public static final String IS_TEMPORARY_FIELD = "is_temporary";


    @DatabaseField(id = true, columnName = ID_FIELD)
    private Long id;

    @DatabaseField(columnName = PRESERVATION_ID_FIELD)
    private Long preservationId;

    @DatabaseField(columnName = UUID_FIELD)
    private String participantUuid;

    @DatabaseField(columnName = MAP_NAME_FIELD)
    private String mapName;

    @Setter
    @DatabaseField(columnName = IS_VISIBLE_FIELD)
    private boolean isVisible;

    @Setter
    @DatabaseField(columnName = IS_TEMPORARY_FIELD)
    private boolean isTemporary;

}
