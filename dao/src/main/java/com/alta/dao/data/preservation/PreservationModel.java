package com.alta.dao.data.preservation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that described the preservation.
 */
@Getter
@DatabaseTable(tableName = "preservations")
public class PreservationModel {

    /**
     * Provides the ID field name.
     */
    public static final String ID_FIELD = "id";

    /**
     * Provides the created at field name.
     */
    public static final String CREATED_AT_FIELD = "created_at";

    /**
     * Provides the chapter indicator field name.
     */
    public static final String CHAPTER_INDICATOR_FIELD = "chapter_indicator";

    @Setter
    @DatabaseField(id = true, columnName = ID_FIELD)
    private Long id;

    @DatabaseField(columnName = CREATED_AT_FIELD)
    private String createdAt;

    @DatabaseField(columnName = CHAPTER_INDICATOR_FIELD)
    private Integer chapterIndicator;

    @DatabaseField(foreign = true, columnName = ID_FIELD, foreignAutoRefresh = true)
    private CharacterPreservationModel characterPreservation;
}
