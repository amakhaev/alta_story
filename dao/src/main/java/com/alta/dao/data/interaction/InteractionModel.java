package com.alta.dao.data.interaction;

import com.alta.dao.data.interaction.effect.InteractionEffectModel;
import com.alta.dao.data.interaction.postProcessing.InteractionPostProcessingModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

/**
 * Provides the model that describes the interaction between participants.
 */
@Getter
@Builder
public class InteractionModel {

    private String uuid;
    private String targetUuid;
    private String nextInteractionUuid;
    private Integer chapterIndicatorFrom;
    private Integer chapterIndicatorTo;
    private Integer shiftTileX;
    private Integer shiftTileY;
    private InteractionConditionModel preCondition;

    @Singular("failedPreConditionEffects")
    private List<InteractionEffectModel> failedPreConditionEffects;

    @Singular("effects")
    private List<InteractionEffectModel> effects;

    @Singular("postProcessors")
    private List<InteractionPostProcessingModel> postProcessors;
}
