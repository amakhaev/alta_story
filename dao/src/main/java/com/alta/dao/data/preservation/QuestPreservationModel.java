package com.alta.dao.data.preservation;

import com.datastax.driver.mapping.annotations.*;
import com.google.gson.annotations.SerializedName;
import lombok.*;

/**
 * Represent preservation of quest in database.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = QuestPreservationModel.TABLE_NAME)
public class QuestPreservationModel {

    public static final String TABLE_NAME = "quest_preservations";

    public static final String PRESERVATION_ID_FIELD = "preservation_id";
    public static final String QUEST_NAME_FIELD = "quest_name";
    public static final String CURRENT_STEP_NUMBER_FIELD = "current_step_number";
    public static final String COMPLETED_FIELD = "completed";

    @PartitionKey(0)
    @SerializedName(PRESERVATION_ID_FIELD)
    @Column(name = PRESERVATION_ID_FIELD)
    private int preservationId;

    @PartitionKey(1)
    @SerializedName(QUEST_NAME_FIELD)
    @Column(name = QUEST_NAME_FIELD)
    private String questName;

    @SerializedName(CURRENT_STEP_NUMBER_FIELD)
    @Column(name = CURRENT_STEP_NUMBER_FIELD)
    private int currentStepNumber;

    @SerializedName(COMPLETED_FIELD)
    @Column(name = COMPLETED_FIELD)
    private boolean isCompleted;

}
