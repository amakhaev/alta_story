package com.alta.dao.data.preservation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;

/**
 * Provides the model that described the preservation of interaction.
 */
@Getter
@DatabaseTable(tableName = "quest_preservations")
public class QuestPreservationModel {

    /**
     * Provides the ID field name.
     */
    public static final String ID_FIELD = "id";

    /**
     * Provides the preservation id field name.
     */
    public static final String PRESERVATION_ID_FIELD = "preservation_id";

    /**
     * Provides the name field name.
     */
    public static final String NAME_FIELD = "name";

    /**
     * Provides the current step number field name.
     */
    public static final String CURRENT_STEP_NUMBER_FIELD = "current_step_number";

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

    @DatabaseField(columnName = NAME_FIELD)
    private String name;

    @DatabaseField(columnName = CURRENT_STEP_NUMBER_FIELD)
    private int currentStepNumber;

    @DatabaseField(columnName = IS_COMPLETED_FIELD)
    private boolean isCompleted;

    @DatabaseField(columnName = IS_TEMPORARY_FIELD)
    private boolean isTemporary;

}
