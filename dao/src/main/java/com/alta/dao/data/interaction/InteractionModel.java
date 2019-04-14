package com.alta.dao.data.interaction;

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
    private boolean showOnce;

    @Singular
    private List<InteractionEffectModel> effects;

}
