package com.alta.mediator.domain.actor;

import com.alta.dao.data.actor.ActorModel;
import com.alta.dao.domain.actor.ActorService;
import com.alta.engine.model.frameStage.ActingCharacterEngineModel;
import com.alta.engine.model.frameStage.NpcEngineModel;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the service that manipulated model related to {@link com.alta.scene.entities.Actor}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ActorDataProviderImpl implements ActorDataProvider {

    private final ActorService actorService;
    private final ActorEngineMapper actorEngineMapper;

    /**
     * Gets the acting character by given skin name
     *
     * @param skinName - the name of skin for character
     * @param startCoordinates - the coordinates of start position for actor
     * @return the {@link ActingCharacterEngineModel}
     */
    @Override
    public ActingCharacterEngineModel getActingCharacter(String skinName, Point startCoordinates, String uuid) {
        ActorModel actorModel = this.actorService.getActorModel(skinName);
        actorModel.setStartMapCoordinates(startCoordinates);
        actorModel.setUuid(uuid);

        return this.actorEngineMapper.doMappingForActingCharacter(actorModel, skinName);
    }

    /**
     * Gets the simple npc by given skin name
     *
     * @param skinName                      - the name of skin for character
     * @param startCoordinates              - the coordinates of start position for actor
     * @param repeatingMovementDurationTime - the time of repeating the participantComputator of simple NPC
     * @return the {@link NpcEngineModel}
     */
    @Override
    public NpcEngineModel getSimpleNpc(String skinName,
                                       Point startCoordinates,
                                       int repeatingMovementDurationTime,
                                       String uuid) {
        ActorModel actorModel = this.actorService.getActorModel(skinName);
        actorModel.setRepeatingMovementDurationTime(actorModel.getRepeatingMovementDurationTime());
        actorModel.setRepeatingMovementDurationTime(repeatingMovementDurationTime);
        actorModel.setStartMapCoordinates(startCoordinates);
        actorModel.setUuid(uuid);

        return this.actorEngineMapper.doMappingForSimpleNpc(actorModel);
    }
}
