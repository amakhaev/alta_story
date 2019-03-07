package com.alta.scene.component.actorAnimation;

import lombok.Builder;
import lombok.Getter;

import java.awt.*;
import java.util.List;

/**
 * Provides the description for animation related to actor
 */
@Getter
@Builder
public class ActorAnimationDescriptor<T> {

    private final String pathToSpriteSheet;
    private final List<Point> animatedTileCoordinates;
    private final int tileWidth;
    private final int tileHeight;
    private final T identifier;
    private final int duration;
    private final int stopFrameIndex;

}
