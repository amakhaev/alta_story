package com.alta.dao.data.quest;

import com.alta.dao.data.common.effect.EffectDataModel;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * Provides the model that contains model related to specific step in quest.
 */
@Data
@Builder
public class QuestStepModel {

    private int stepNumber;
    private String triggerType;
    private String triggerMap;
    private String targetUuid;
    private int chapterIndicatorFrom;
    private int chapterIndicatorTo;

    @Singular
    private List<EffectDataModel> effects;

}
