package com.alta.dao.data.interaction;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.interaction.postProcessing.InteractionPostProcessingModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.awt.*;
import java.util.List;

/**
 * Provides the model that describes the interaction between participants.
 */
@Getter
@Builder
public class InteractionDataModel {

    private String uuid;
    private String targetUuid;
    private String nextInteractionUuid;
    private Integer chapterIndicatorFrom;
    private Integer chapterIndicatorTo;
    private InteractionConditionModel preCondition;

    @Singular("shiftTiles")
    private List<Point> shiftTiles;

    @Singular("failedPreConditionEffects")
    private List<EffectDataModel> failedPreConditionEffects;

    @Singular("effects")
    private List<EffectDataModel> effects;

    @Singular("postProcessors")
    private List<InteractionPostProcessingModel> postProcessors;
}
