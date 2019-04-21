package com.alta.dao.data.preservation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

/**
 * Provides the model that described the preservation of interaction.
 */
@Getter
@DatabaseTable(tableName = "interaction_preservations")
public class InteractionPreservationModel {

    @Builder
    private static InteractionPreservationModel create(Long id,
                                                       @NonNull Long preservationId,
                                                       @NonNull String uuid,
                                                       @NonNull String mapName,
                                                       boolean isComplete,
                                                       boolean isTemporary) {
        InteractionPreservationModel interactionPreservationModel = new InteractionPreservationModel();
        interactionPreservationModel.id = id;
        interactionPreservationModel.uuid = uuid;
        interactionPreservationModel.isCompleted = isComplete;
        interactionPreservationModel.isTemporary = isTemporary;
        interactionPreservationModel.preservationId = preservationId;
        interactionPreservationModel.mapName = mapName;

        return interactionPreservationModel;
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
     * Provides the is complete field name.
     */
    public static final String IS_COMPLETED_FIELD = "is_completed";

    /**
     * Provides the is temporary field name.
     */
    public static final String IS_TEMPORARY_FIELD = "is_temporary";

    @DatabaseField(id = true, columnName = ID_FIELD)
    private Long id;

    @DatabaseField(columnName = PRESERVATION_ID_FIELD)
    private Long preservationId;

    @DatabaseField(columnName = UUID_FIELD)
    private String uuid;

    @DatabaseField(columnName = MAP_NAME_FIELD)
    private String mapName;

    @Setter
    @DatabaseField(columnName = IS_COMPLETED_FIELD)
    private boolean isCompleted;

    @Setter
    @DatabaseField(columnName = IS_TEMPORARY_FIELD)
    private boolean isTemporary;
}
