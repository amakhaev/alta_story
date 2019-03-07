package com.alta.scene.component.actorAnimation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the animation specified for Actor entity
 */
@Slf4j
public class ActorAnimation<T> {

    private Map<T, AnimationInternal> animations;
    @Getter
    private T currentIdentifier;

    /**
     * Initialize ew instance of {@link ActorAnimation}
     */
    public ActorAnimation() {
        this.animations = new HashMap<>();
    }

    /**
     * Adds the animation by given descriptor
     *
     * @param descriptor - the descriptor that
     */
    public void addAnimation(ActorAnimationDescriptor<T> descriptor) {
        if (descriptor == null) {
            log.warn("Null descriptor have got for animation creating");
            return;
        }

        SpriteSheet spriteSheet = this.createSpriteSheet(
                descriptor.getPathToSpriteSheet(),
                descriptor.getTileWidth(),
                descriptor.getTileHeight()
        );

        if (spriteSheet == null) {
            return;
        }

        Animation animation = this.createAnimation(
                spriteSheet,
                descriptor.getAnimatedTileCoordinates(),
                descriptor.getDuration()
        );
        this.animations.put(descriptor.getIdentifier(), new AnimationInternal(animation, descriptor.getStopFrameIndex()));
    }

    /**
     * Sets the auto update state of animation
     *
     * @param isAuto - indicates when animation should be updated automatically
     */
    public void setAutoupdate(boolean isAuto) {
        if (this.currentIdentifier == null ||
                !this.animations.containsKey(this.currentIdentifier) ||
                this.animations.get(this.currentIdentifier).animation == null) {
            log.warn("No selected animation");
            return;
        }

        if (isAuto) {
            this.animations.get(this.currentIdentifier).animation.setAutoUpdate(true);
            return;
        }

        this.animations.get(this.currentIdentifier).animation.setAutoUpdate(false);
        this.animations.get(this.currentIdentifier).animation.setCurrentFrame(
                this.animations.get(this.currentIdentifier).stopFrameIndex
        );
    }

    /**
     * Sets current animation by given identifier
     *
     * @param identifier - the identifier of animation
     */
    public void setCurrentAnimation(T identifier) {
        if (!this.animations.containsKey(identifier)) {
            log.warn("The animation with given identifier not found: {}", identifier);
            this.currentIdentifier = null;
            return;
        }

        this.currentIdentifier = identifier;
    }

    /**
     * Gets the animation that used in current moment
     *
     * @return the {@link Animation} instance
     */
    public Animation getCurrentAnimation() {
        return this.animations.get(this.currentIdentifier) == null ?
                null :
                this.animations.get(this.currentIdentifier).animation;
    }

    private SpriteSheet createSpriteSheet(String path, int tileWidth, int tileHeight) {
        try {
            return new SpriteSheet(
                    path,
                    tileWidth,
                    tileHeight
            );
        } catch (SlickException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private Animation createAnimation(SpriteSheet spriteSheet, List<Point> tileCoordinates, int duration) {
        org.newdawn.slick.Image[] animationImages = tileCoordinates.stream()
                .map(coordinate -> spriteSheet.getSubImage(coordinate.x, coordinate.y))
                .toArray(org.newdawn.slick.Image[]::new);

        Animation animation = new Animation(animationImages, duration);
        animation.setAutoUpdate(false);
        return animation;
    }

    @AllArgsConstructor
    private class AnimationInternal {
        private Animation animation;
        private int stopFrameIndex;
    }
}
