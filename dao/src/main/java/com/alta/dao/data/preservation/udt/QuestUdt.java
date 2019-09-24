package com.alta.dao.data.preservation.udt;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import lombok.*;

/**
 * Represent preservation of quest UDT in database.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UDT(name = "quest")
public class QuestUdt {

    @Field(name = "quest_name")
    private String questName;

    @Field(name = "current_step_number")
    private int currentStepNumber;

    @Field(name = "completed")
    private boolean isCompleted;

}
