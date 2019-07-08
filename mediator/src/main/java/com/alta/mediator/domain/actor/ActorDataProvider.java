package com.alta.mediator.domain.actor;

import com.alta.behaviorprocess.data.common.FaceSetDescription;
import com.alta.dao.domain.map.internalEntities.AlterableNpcEntity;
import com.alta.dao.domain.map.internalEntities.SimpleNpcEntity;
import com.alta.engine.data.frameStage.ActingCharacterEngineModel;
import com.alta.engine.data.frameStage.NpcEngineModel;

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
     * Gets the simple npc by given npc entity.
     *
     * @param npcEntity - the npc entity to create engine model.
     * @return the {@link NpcEngineModel}
     */
    NpcEngineModel getSimpleNpc(SimpleNpcEntity npcEntity);

    /**
     * Gets the alterable npc by given npc entity.
     *
     * @param npcEntity - the npc entity to create engine model.
     * @return the {@link NpcEngineModel} instance.
     */
    NpcEngineModel getAlterableNpc(AlterableNpcEntity npcEntity);

    /**
     * Gets the face set descriptor for given actor.
     *
     * @param actorName - the name of actor.
     * @return the {@link FaceSetDescription} instance or null.
     */
    FaceSetDescription getFaceSetForActor(String actorName);
}
