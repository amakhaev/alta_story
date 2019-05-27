package com.alta.interaction.interactionOnMap;

import com.alta.interaction.data.EffectModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

/**
 * Provides the model that describes the interaction.
 */
@Getter
@Builder
public class InteractionModel {

    private final String uuid;
    private final String targetUuid;
    private final InteractionModel next;
    private final List<EffectModel> interactionEffects;
    private final List<EffectModel> failedPreConditionInteractionEffects;
    private List<Point> shiftTiles;
    private Function<Void, Boolean> preCondition;

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
