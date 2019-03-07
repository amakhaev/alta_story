package com.alta.computator.service.movement;

import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.movement.strategy.MovementDirection;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the computator for acting character
 */
public class ActingCharacterComputator {

    @Getter
    private ActorParticipant actorParticipant;

    /**
     * Initialize new instance of {@link ActingCharacterComputator}
     */
    public ActingCharacterComputator(ActorParticipant participant) {
        this.actorParticipant = participant;
        this.actorParticipant.setCurrentDirection(MovementDirection.DOWN);
    }

    /**
     * Handles the computing of map facility objects
     *
     * @param focusPointMapCoordinates - the map coordinates of focus point
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     * @param direction - the direction of last moving.
     * @param isMoving - indicates when move process is running
     */
    public void onCompute(Point focusPointMapCoordinates,
                          Point focusPointGlobalCoordinates,
                          MovementDirection direction,
                          boolean isMoving) {
        if (this.actorParticipant.getCurrentMapCoordinates() == null ||
                this.actorParticipant.getCurrentMapCoordinates().x != focusPointMapCoordinates.x ||
                this.actorParticipant.getCurrentMapCoordinates().y != focusPointMapCoordinates.y) {
            this.actorParticipant.updateCurrentMapCoordinates(
                    focusPointMapCoordinates.x,
                    focusPointMapCoordinates.y
            );
        }

        if (this.actorParticipant.getCurrentGlobalCoordinates() == null ||
                this.actorParticipant.getCurrentGlobalCoordinates().x != focusPointGlobalCoordinates.x ||
                this.actorParticipant.getCurrentGlobalCoordinates().y != focusPointGlobalCoordinates.y) {
            this.actorParticipant.updateCurrentGlobalCoordinates(
                    focusPointGlobalCoordinates.x,
                    focusPointGlobalCoordinates.y
            );
        }

        if (this.actorParticipant.getCurrentDirection() != direction) {
            this.actorParticipant.setCurrentDirection(direction);
        }
        this.actorParticipant.setMoving(isMoving);
    }

}
