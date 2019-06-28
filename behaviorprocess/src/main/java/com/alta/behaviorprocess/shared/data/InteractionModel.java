package com.alta.behaviorprocess.shared.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

/**
 * Provides the data that describes the interaction.
 */
@Getter
@Builder
public class InteractionModel {

    private final String uuid;
    private final String targetUuid;
    private final String mapName;
    private final List<EffectModel> interactionEffects;
    private final List<EffectModel> failedPreConditionInteractionEffects;
    private List<Point> shiftTiles;
    private Function<Void, Boolean> preCondition;

    @Setter
    private InteractionModel next;

    @Setter
    private boolean isCompleted;

    /**
     * Finds the interaction that wasn't completed.
     *
     * @return the {@link InteractionModel} instance or null if all already completed.
     */
    public InteractionModel findIncompletedInteraction() {
        return this.findIncompletedInteractionInternal(this);
    }

    /**
     * Finds the last interaction.
     *
     * @return the {@link InteractionModel} instance.
     */
    public InteractionModel findLastInteraction() {
        return this.findLastInteractionInternal(this);
    }

    private InteractionModel findIncompletedInteractionInternal(InteractionModel interaction) {
        if (!interaction.isCompleted) {
            return interaction;
        }

        return interaction.next == null ? null : interaction.findIncompletedInteractionInternal(interaction.next);
    }

    private InteractionModel findLastInteractionInternal(InteractionModel interaction) {
        return interaction.next == null ? interaction : interaction.findLastInteractionInternal(interaction.next);
    }

}
