package com.alta.dao.data.interaction;

import lombok.Builder;
import lombok.Getter;

/**
 * Provides model that determinate the condition for interaction
 */
@Getter
@Builder
public class InteractionConditionModel {

    private final String uuid;
    private final ConditionType conditionType;

    public enum ConditionType {
        INTERACTION_COMPLETED
    }
}
