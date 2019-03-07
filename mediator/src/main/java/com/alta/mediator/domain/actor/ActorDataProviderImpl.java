package com.alta.mediator.domain.actor;

import com.alta.dao.domain.actor.ActorService;
import com.alta.engine.data.ActingCharacterModel;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
     * @return the {@link ActingCharacterModel}
     */
    @Override
    public ActingCharacterModel getActingCharacter(String skinName) {
        return this.actorEngineMapper.doMappingForActingCharacter(this.actorService.getActorModel(skinName));
    }
}
