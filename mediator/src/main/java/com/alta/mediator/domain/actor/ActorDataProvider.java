package com.alta.mediator.domain.actor;

import com.alta.engine.sceneComponent.actor.ActingCharacterEngineModel;
import com.alta.engine.sceneComponent.actor.SimpleNpcEngineModel;

import java.awt.*;

/**
 * Provides the service that manipulated data related to {@link com.alta.scene.entities.Actor}
 */
public interface ActorDataProvider {

    /**
     * Gets the acting character by given skin name
     *
     * @param skinName - the name of skin for character
     * @param startCoordinates - the coordinates of start position for actor
     * @return the {@link ActingCharacterEngineModel}
     */
    ActingCharacterEngineModel getActingCharacter(String skinName, Point startCoordinates);

    /**
     * Gets the simple npc by given skin name
     *
     * @param skinName - the name of skin for character
     * @param startCoordinates - the coordinates of start position for actor
     * @param repeatingMovementDurationTime - the time of repeating the movement of simple NPC
     * @return the {@link SimpleNpcEngineModel}
     */
    SimpleNpcEngineModel getSimpleNpc(String skinName, Point startCoordinates, int repeatingMovementDurationTime);
}
