package com.alta.dao.data.quest;

import com.alta.dao.data.common.effect.EffectDataModel;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * Provides the model that contains data related to specific step in quest.
 */
@Data
@Builder
public class QuestStepModel {

    private int stepNumber;
    private String triggerType;
    private String triggerMap;
    private String targetUuid;

    @Singular
    private List<EffectDataModel> effects;

}
