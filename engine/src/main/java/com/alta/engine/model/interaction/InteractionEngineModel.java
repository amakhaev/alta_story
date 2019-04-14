package com.alta.engine.model.interaction;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Provides the model to be described interactions between participants on scene.
 */
@Getter
@Builder
public class InteractionEngineModel {

    private String uuid;
    private String targetUuid;
    private boolean showOnce;
    private InteractionEngineModel next;

    private List<InteractionEffectEngineModel> interactionEffects;
}
