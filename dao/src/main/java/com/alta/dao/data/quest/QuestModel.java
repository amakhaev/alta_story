package com.alta.dao.data.quest;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * Provides the model that contains model for the quest.
 */
@Data
@Builder
public class QuestModel {

    private String uuid;
    private String name;
    private String displayName;

    @Singular
    private List<QuestStepModel> steps;

}
