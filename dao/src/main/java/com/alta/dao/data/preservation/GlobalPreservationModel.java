package com.alta.dao.data.preservation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that described the global preservation.
 */
@Getter
@DatabaseTable(tableName = "global_preservations")
public class GlobalPreservationModel {

    /**
     * Provides the ID field name.
     */
    public static final String ID_FIELD = "id";

    /**
     * Provides the character indicator field name.
     */
    public static final String CHAPTER_INDICATOR_FIELD = "chapter_indicator";

    /**
     * Provides the is temporary field name.
     */
    public static final String IS_TEMPORARY_FIELD = "is_temporary";

    /**
     * Provides the preservation id field name.
     */
    public static final String PRESERVATION_ID_FIELD = "preservation_id";

    @DatabaseField(id = true, columnName = ID_FIELD)
    private Long id;

    @Setter
    @DatabaseField(columnName = CHAPTER_INDICATOR_FIELD)
    private Integer chapterIndicator;

    @Setter
    @DatabaseField(columnName = IS_TEMPORARY_FIELD)
    private boolean isTemporary;

    @DatabaseField(columnName = PRESERVATION_ID_FIELD)
    private Long preservationId;

}
