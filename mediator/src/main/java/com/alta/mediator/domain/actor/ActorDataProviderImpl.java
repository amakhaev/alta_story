package com.alta.mediator.domain.actor;

import com.alta.dao.domain.actor.ActorService;
import com.alta.engine.data.ActingCharacterEngineModel;
import com.alta.engine.data.SimpleNpcEngineModel;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the service that manipulated data related to {@link com.alta.scene.entities.Actor}
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
                this.actorService.getActorModel(skinName, startCoordinates)
        );
    }

    /**
     * Gets the simple npc by given skin name
     *
     * @param skinName         - the name of skin for character
     * @param startCoordinates - the coordinates of start position for actor
     * @return the {@link SimpleNpcEngineModel}
     */
    @Override
    public SimpleNpcEngineModel getSimpleNpc(String skinName, Point startCoordinates) {
        return this.actorEngineMapper.doMappingForSimpleNpc(
                this.actorService.getActorModel(skinName, startCoordinates)
        );
    }
}
