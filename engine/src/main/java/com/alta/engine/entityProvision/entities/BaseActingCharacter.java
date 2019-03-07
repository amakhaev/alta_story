package com.alta.engine.entityProvision.entities;

import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.scene.component.actorAnimation.ActorAnimation;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import com.alta.scene.entities.Actor;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.GameContainer;

import java.util.List;

/**
 * Provides the acting character
 */
@Slf4j
public class BaseActingCharacter implements Actor<ActorParticipant> {

    private static final MovementDirection DEFAULT_ANIMATION = MovementDirection.DOWN;

    private final List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors;
    private final ActorAnimation<MovementDirection> actorAnimation;
    private int stopAnimationCounter;

    /**
     * Initialize new instance of BaseActingCharacter
     */
    public BaseActingCharacter(List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors) {
        this.animationDescriptors = animationDescriptors;
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
        }
    }

    /**
     * Renders the object on given coordinates
     *
     * @param actorParticipant - the actor participant that stored data for rendering
     */
    @Override
    public void render(ActorParticipant actorParticipant) {
        if (this.actorAnimation.getCurrentIdentifier() == null || this.actorAnimation.getCurrentAnimation() == null) {
            log.error("No current animation for render acting character: {}", actorParticipant.getUuid());
            return;
        }

        this.actorAnimation.getCurrentAnimation().draw(
                actorParticipant.getCurrentGlobalCoordinates().x,
                actorParticipant.getCurrentGlobalCoordinates().y
        );
    }

    /**
     * Updates the actor state
     *
     * @param data  - provides the data to update
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

        // data.isMoving returns true when actor is moving. Sometimes it can be stopped shortly then appears lags in
        // animation. Need to check that data.isMoving() return false several times in a row to avoid this issue.
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
