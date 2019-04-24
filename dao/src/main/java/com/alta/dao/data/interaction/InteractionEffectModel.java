package com.alta.dao.data.interaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides the description of effect from interaction.
 */
@Getter
@AllArgsConstructor
public class InteractionEffectModel {

    private final InteractionEffectType type;

    public enum InteractionEffectType {
        DIALOGUE,
        HIDE_FACILITY
    }
}
