package com.alta.engine.model.interaction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Provides the model to be described interactions between participants on scene.
 */
@Getter
@Builder
public class InteractionEngineModel {

    private final String uuid;
    private final String targetUuid;
    private final InteractionEngineModel next;
    private final List<InteractionEffectEngineModel> interactionEffects;

    @Setter
    private boolean isCompleted;

    /**
     * Finds the interaction that wasn't completed.
     *
     * @return the {@link InteractionEngineModel} instance or null if all already completed.
     */
    public InteractionEngineModel findIncompletedInteraction() {
        return this.findIncompletedInteractionInternal(this);
    }

    /**
     * Finds the last interaction.
     *
     * @return the {@link InteractionEngineModel} instance.
     */
    public InteractionEngineModel findLastInteraction() {
        return this.findLastInteractionInternal(this);
    }

    private InteractionEngineModel findIncompletedInteractionInternal(InteractionEngineModel interaction) {
        if (!interaction.isCompleted) {
            return interaction;
        }

        return interaction.next == null ? null : interaction.findIncompletedInteractionInternal(interaction.next);
    }

    private InteractionEngineModel findLastInteractionInternal(InteractionEngineModel interaction) {
        return interaction.next == null ? interaction : interaction.findLastInteractionInternal(interaction.next);
    }
}
