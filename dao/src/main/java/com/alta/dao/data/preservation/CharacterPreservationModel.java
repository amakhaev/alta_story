package com.alta.dao.data.preservation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Builder;
import lombok.Getter;

/**
 * Provides the preservation model
 */
@Getter
@DatabaseTable(tableName = "character_preservations")
public class CharacterPreservationModel {

    @Builder
    public static CharacterPreservationModel create(Long id, String mapName, Integer focusX, Integer focusY, String skin) {
        CharacterPreservationModel model = new CharacterPreservationModel();
        model.id = id;
        model.mapName = mapName;
        model.focusX = focusX;
        model.focusY = focusY;
        model.mainCharaterSkin = skin;
        return model;
    }

    /**
     * Provides the ID field name.
     */
    public static final String ID_FIELD = "id";

    /**
     * Provides the map name field name.
     */
    public static final String MAP_NAME_FIELD = "map_name";

    /**
     * Provides the focus x field name.
     */
    public static final String FOCUS_X_FIELD = "focus_x";

    /**
     * Provides the focus y field name.
     */
    public static final String FOCUS_Y_FIELD = "focus_y";

    /**
     * Provides the main character skin field name.
     */
    public static final String MAIN_CHARACTER_SKIN_FIELD = "main_character_skin";

    @DatabaseField(id = true, columnName = ID_FIELD)
    private Long id;

    @DatabaseField(columnName = MAP_NAME_FIELD)
    private String mapName;

    @DatabaseField(columnName = FOCUS_X_FIELD)
    private Integer focusX;

    @DatabaseField(columnName = FOCUS_Y_FIELD)
    private Integer focusY;

    @DatabaseField(columnName = MAIN_CHARACTER_SKIN_FIELD)
    private String mainCharaterSkin;
}
