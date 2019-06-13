package com.alta.engine.data.interaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the engine data that describes the effect of interaction.
 */
@Getter
@AllArgsConstructor
public abstract class InteractionEffectEngineModel {

    private final EffectType type;

    public enum EffectType {
        DIALOGUE,
        HIDE_FACILITY,
        SHOW_FACILITY
    }
}
