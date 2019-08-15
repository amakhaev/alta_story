package com.alta.engine.data.frameStage;

import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.awt.*;
import java.util.List;

/**
 * Provides the engine model of acting character
 */
@Getter
@Builder
public class ActingCharacterEngineModel {

    @Singular
    private List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors;
    private Point startMapCoordinates;
    private String uuid;
    private int zIndex;
    private String skinName;

}
