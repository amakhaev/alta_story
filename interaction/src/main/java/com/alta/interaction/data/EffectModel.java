package com.alta.interaction.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the engine data that describes the effect of interaction.
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
