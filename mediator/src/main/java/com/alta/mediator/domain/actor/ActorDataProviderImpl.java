package com.alta.mediator.domain.actor;

import com.alta.dao.data.actor.ActorModel;
import com.alta.dao.domain.actor.ActorService;
import com.alta.engine.model.frameStage.ActingCharacterEngineModel;
import com.alta.engine.model.frameStage.SimpleNpcEngineModel;
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
    public ActingCharacterEngineModel getActingCharacter(String skinName, Point startCoordinates) {
        return this.actorEngineMapper.doMappingForActingCharacter(
                this.actorService.getActorModel(skinName, startCoordinates), skinName
        );
    }

    /**
     * Gets the simple npc by given skin name
     *
     * @param skinName                      - the name of skin for character
     * @param startCoordinates              - the coordinates of start position for actor
     * @param repeatingMovementDurationTime - the time of repeating the movement of simple NPC
     * @param dialogueText                  - the time of repeating the movement of simple NPC
     * @return the {@link SimpleNpcEngineModel}
     */
    @Override
    public SimpleNpcEngineModel getSimpleNpc(String skinName,
                                             Point startCoordinates,
                                             int repeatingMovementDurationTime,
                                             String dialogueText) {
        ActorModel actorModel = this.actorService.getActorModel(skinName, startCoordinates);
        actorModel.setRepeatingMovementDurationTime(actorModel.getRepeatingMovementDurationTime());
        actorModel.setRepeatingMovementDurationTime(repeatingMovementDurationTime);
        return this.actorEngineMapper.doMappingForSimpleNpc(actorModel, dialogueText);
    }
}
