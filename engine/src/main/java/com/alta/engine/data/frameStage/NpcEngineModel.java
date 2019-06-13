package com.alta.engine.data.frameStage;

import com.alta.computator.service.movement.directionCalculation.MovementDirection;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import lombok.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Provides the data that describes the simple npc
 */
@Getter
@Builder
public class NpcEngineModel {

    private final String uuid;
    private final Point startMapCoordinates;
    private final int zIndex;
    private final FaceSetDescription faceSetDescriptor;

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
    private String movementSpeed;

    @Setter
    private List<RouteDescription> routeDescription;

    @Singular("animationDescriptors")
    private List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors;

    @Getter
    @AllArgsConstructor
    public static class RouteDescription {
        private int x;
        private int y;
        private String finalDirection;
    }

    @Getter
    @AllArgsConstructor
    public static class FaceSetDescription {
        private int tileWidth;
        private int tileHeight;
        private Map<String, Point> emotions;
        private String pathToImageSet;
    }
}
