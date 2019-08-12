package com.alta.behaviorprocess.shared.scenario.senarioEffects;

import com.alta.behaviorprocess.data.effect.EffectModel;
import com.alta.behaviorprocess.data.effect.HideFacilityEffectModel;
import com.alta.behaviorprocess.data.effect.RouteMovementEffectModel;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;
import lombok.Setter;

public class RouteMovementEffect implements Effect {

    private final EffectListener effectListener;

    @Setter
    private Runnable completeCallback;

    /**
     * Initialize new instance of {@link RouteMovementEffect}.
     *
     * @param effectListener - the listener of effects.
     */
    @AssistedInject
    public RouteMovementEffect(@Assisted @NonNull EffectListener effectListener) {
        this.effectListener = effectListener;
    }

    /**
     * Starts the interaction.
     *
     * @param effect - the effect to be performed.
     */
    @Override
    public void start(EffectModel effect) {
        if (effect.getType() != EffectModel.EffectType.ROUTE_MOVEMENT) {
            throw new ClassCastException(
                    "The effect has " + effect.getType() + " type but required " + EffectModel.EffectType.ROUTE_MOVEMENT
            );
        }

        RouteMovementEffectModel effectModel = (RouteMovementEffectModel) effect;
        this.effectListener.onRouteMovement(effectModel.getTargetUuid(), effectModel.getX(), effectModel.getY());
        this.completeCallback.run();
    }

    /**
     * Triggers the next step in interaction.
     */
    @Override
    public void triggerNext() {

    }
}
