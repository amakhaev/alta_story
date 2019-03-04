package com.alta.computator.service.movement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.actor.ActorParticipant;
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
    }

    /**
     * Handles the computing of map facility objects
     *
     * @param focusPointMapCoordinates - the map coordinates of focus point
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     */
    public void onCompute(Point focusPointMapCoordinates, Point focusPointGlobalCoordinates) {
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
            this.actorParticipant.updateCurrentMapCoordinates(
                    focusPointGlobalCoordinates.x,
                    focusPointGlobalCoordinates.y
            );
        }
    }

}
