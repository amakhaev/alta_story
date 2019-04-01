package com.alta.engine.view.components.actor;

import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.scene.component.actorAnimation.ActorAnimation;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import com.alta.scene.entities.Actor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;

import java.util.List;

/**
 * Provides the actor character component
 */
@Slf4j
public class ActorCharacterComponent implements Actor<ActorParticipant> {

    private static final MovementDirection DEFAULT_ANIMATION = MovementDirection.DOWN;

    private final List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors;
    private final ActorAnimation<MovementDirection> actorAnimation;
    private int stopAnimationCounter;

    @Getter
    private final String uuid;

    /**
     * Initialize new instance of BaseSimpleNpc
     */
    public ActorCharacterComponent(List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors, String uuid) {
        this.animationDescriptors = animationDescriptors;
        this.uuid = uuid;
        this.actorAnimation = new ActorAnimation<>();
    }

    /**
     * Initializes the renderable object in GL context if needed.
     *
     * @param container - the game container instance.
     */
    @Override
    public void initialize(GameContainer container) {
        if (this.animationDescriptors != null && !this.animationDescriptors.isEmpty()) {
            this.animationDescriptors.forEach(this.actorAnimation::addAnimation);
            this.actorAnimation.setCurrentAnimation(DEFAULT_ANIMATION);
        }
    }

    /**
     * Renders the object on given coordinates
     *
     * @param actingCharacterParticipant - the actor participant that stored model for rendering
     */
    @Override
    public void render(ActorParticipant actingCharacterParticipant) {
        if (this.actorAnimation.getCurrentIdentifier() == null || this.actorAnimation.getCurrentAnimation() == null) {
            log.error("No current animation for render actor character: {}", actingCharacterParticipant.getUuid());
            return;
        }

        this.actorAnimation.getCurrentAnimation().draw(
                actingCharacterParticipant.getCurrentGlobalCoordinates().x,
                actingCharacterParticipant.getCurrentGlobalCoordinates().y
        );
    }

    /**
     * Updates the actor state
     *
     * @param data  - provides the model to update
     * @param delta - the time between last and previous calls
     */
    @Override
    public void update(ActorParticipant data, int delta) {
        if (this.actorAnimation.getCurrentAnimation() != null) {
            this.actorAnimation.getCurrentAnimation().update(delta);
        }

        if (data.getCurrentDirection() == null) {
            this.actorAnimation.setCurrentAnimation(DEFAULT_ANIMATION);
            return;
        }

        if (this.actorAnimation.getCurrentIdentifier() != data.getCurrentDirection()) {
            this.actorAnimation.setCurrentAnimation(data.getCurrentDirection());
        }

        // model.isMoving returns true when actor is moving. Sometimes it can be stopped shortly then appears lags in
        // animation. Need to check that model.isMoving() return false several times in a row to avoid this issue.
        if (data.isMoving()) {
            this.actorAnimation.setAutoupdate(data.isMoving());
            this.stopAnimationCounter = 0;
        } else {
            this.stopAnimationCounter++;
            if (this.stopAnimationCounter >= 5) {
                this.actorAnimation.setAutoupdate(data.isMoving());
            }
        }
    }
}
