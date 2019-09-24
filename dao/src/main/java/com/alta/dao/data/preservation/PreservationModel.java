package com.alta.dao.data.preservation;

import com.alta.dao.data.preservation.udt.ActingCharacterUdt;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.gson.annotations.SerializedName;
import lombok.*;

/**
 * Represent preservation of preservation in database.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = PreservationModel.TABLE_NAME)
public class PreservationModel {

    public static final String TABLE_NAME = "preservations";

    public static final String ID_FIELD = "id";
    public static final String CHAPTER_INDICATOR_FIELD = "chapter_indicator";
    public static final String ACTING_CHARACTER_FIELD = "acting_character";

    @PartitionKey
    @Column(name = PreservationModel.ID_FIELD)
    private int id;

    @SerializedName(CHAPTER_INDICATOR_FIELD)
    @Column(name = PreservationModel.CHAPTER_INDICATOR_FIELD)
    private int chapterIndicator;

    @Frozen
    @SerializedName(ACTING_CHARACTER_FIELD)
    @Column(name = PreservationModel.ACTING_CHARACTER_FIELD)
    private ActingCharacterUdt actingCharacter;

}
