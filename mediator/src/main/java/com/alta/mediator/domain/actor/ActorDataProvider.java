package com.alta.mediator.domain.actor;

import com.alta.engine.model.frameStage.ActingCharacterEngineModel;
import com.alta.engine.model.frameStage.NpcEngineModel;

import java.awt.*;

/**
 * Provides the service that manipulated model related to {@link com.alta.scene.entities.Actor}
 */
public interface ActorDataProvider {

    /**
     * Gets the acting character by given skin name
     *
     * @param skinName - the name of skin for character
     * @param startCoordinates - the coordinates of start position for actor
     * @param uuid - the uuid of simple acting character.
     * @return the {@link ActingCharacterEngineModel}
     */
    ActingCharacterEngineModel getActingCharacter(String skinName, Point startCoordinates, String uuid);

    /**
     * Gets the simple npc by given skin name
     *
     * @param skinName                      - the name of skin for character
     * @param startCoordinates              - the coordinates of start position for actor
     * @param repeatingMovementDurationTime - the time of repeating the participantComputator of simple NPC
     * @param dialogueText                  - the time of repeating the participantComputator of simple NPC
     * @param uuid                          - the uuid of simple npc.
     * @return the {@link NpcEngineModel}
     */
    NpcEngineModel getSimpleNpc(String skinName,
                                Point startCoordinates,
                                int repeatingMovementDurationTime,
                                String uuid);
}
