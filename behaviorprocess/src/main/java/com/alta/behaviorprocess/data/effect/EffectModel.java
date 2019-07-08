package com.alta.behaviorprocess.data.effect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the engine model that describes the effect of interaction.
 */
@Getter
@AllArgsConstructor
public abstract class EffectModel {

    private final EffectType type;

    public enum EffectType {
        DIALOGUE,
        HIDE_FACILITY,
        SHOW_FACILITY
    }
}
