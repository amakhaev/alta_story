package com.alta.behaviorprocess.data.quest;

import lombok.Builder;
import lombok.Data;

/**
 * Provides the model that describes the quest.
 */
@Data
@Builder
public class QuestModel {

    private final String name;
    private final String displayName;
    private final QuestStepModel currentStep;

}
