package com.alta.dao.domain.preservation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;

/**
 * Provides the preservation model
 */
@Getter
@DatabaseTable(tableName = "preservation")
public class PreservationModel {

    /**
     * Provides the ID field name.
     */
    public static final String ID_FIELD = "id";

    /**
     * Provides the map name field name.
     */
    public static final String MAP_NAME_FIELD = "map_name";

    @DatabaseField(id = true, columnName = ID_FIELD)
    private int id;

    @DatabaseField(columnName = MAP_NAME_FIELD)
    private String mapName;
}
