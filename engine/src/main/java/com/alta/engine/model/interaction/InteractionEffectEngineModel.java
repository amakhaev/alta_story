package com.alta.engine.model.interaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the engine model that describes the effect of interaction.
 */
@Getter
@AllArgsConstructor
public abstract class InteractionEffectEngineModel {

    private final EffectType type;

    public enum EffectType {
        DIALOGUE,
        HIDE_FACILITY
    }
}
