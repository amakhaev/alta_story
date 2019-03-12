package com.alta.engine.data;

import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.awt.*;
import java.util.List;

/**
 * Provides the model that describes the simple npc
 */
@Getter
@Builder
public class SimpleNpcEngineModel {

    private final String uuid;
    private final Point startMapCoordinates;
    private final int zIndex;
    private final int repeatingMovementDurationTime;

    @Singular
    private List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors;

}
