package com.alta.dao.data.common.effect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the description of effect from interaction.
 */
@Getter
@AllArgsConstructor
public class EffectDataModel {

    private final InteractionEffectType type;

    public enum InteractionEffectType {
        DIALOGUE,
        HIDE_FACILITY,
        SHOW_FACILITY
    }
}
