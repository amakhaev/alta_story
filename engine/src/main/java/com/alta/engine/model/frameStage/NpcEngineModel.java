package com.alta.engine.model.frameStage;

import com.alta.computator.service.movement.directionCalculation.MovementDirection;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.awt.*;
import java.util.List;

/**
 * Provides the model that describes the simple npc
 */
@Getter
@Builder
public class NpcEngineModel {

    private final String uuid;
    private final Point startMapCoordinates;
    private final int zIndex;

    @Setter
    private boolean isMovementRouteLooped;

    @Setter
    private String movementStrategy;

    @Setter
    private String initialDirection;

    @Setter
    private boolean isAnimatedAlways;

    @Setter
    private int repeatingMovementDurationTime;

    @Setter
    private List<Point> movementRoute;

    @Singular("animationDescriptors")
    private List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors;

}
