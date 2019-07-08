package com.alta.behaviorprocess.data.quest;

import com.alta.behaviorprocess.data.effect.EffectModel;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * Provides the model that describes the step of the quest.
 */
@Data
@Builder
public class QuestStepModel {

    private int stepNumber;
    private QuestStepTriggerType triggerType;
    private String triggerMap;
    private String targetUuid;
    private int chapterIndicatorFrom;
    private int chapterIndicatorTo;

    @Singular
    private List<EffectModel> effects;

}
