package com.alta.dao.data.preservation.udt;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.gson.annotations.SerializedName;
import lombok.*;

/**
 * Represent acting character UDT in database.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UDT(name = ActingCharacterUdt.UDT_NAME)
public class ActingCharacterUdt {

    public static final String UDT_NAME = "acting_character";

    public static final String MAP_NAME_FIELD = "map_name";
    public static final String FOCUS_X_FIELD = "focus_x";
    public static final String FOCUS_Y_FIELD = "focus_y";
    public static final String SKIN_FIELD = "skin";

    @Field(name = MAP_NAME_FIELD)
    @SerializedName(MAP_NAME_FIELD)
    private String mapName;

    @Field(name = FOCUS_X_FIELD)
    @SerializedName(FOCUS_X_FIELD)
    private int focusX;

    @Field(name = FOCUS_Y_FIELD)
    @SerializedName(FOCUS_Y_FIELD)
    private int focusY;

    @Field(name = SKIN_FIELD)
    @SerializedName(SKIN_FIELD)
    private String skin;

}
