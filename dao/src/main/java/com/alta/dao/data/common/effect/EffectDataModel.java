package com.alta.dao.data.common.effect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the description of effect from interaction.
 */
@Getter
@AllArgsConstructor
public class EffectDataModel {

    private final EffectType type;

    public enum EffectType {
        // UI effects
        DIALOGUE,
        HIDE_FACILITY,
        SHOW_FACILITY,

        // Background effects
        INCREMENT_CHAPTER_INDICATOR
    }
}
