package com.alta.mediator.domain.actor;

import com.alta.engine.data.ActingCharacterModel;

/**
 * Provides the service that manipulated data related to {@link com.alta.scene.entities.Actor}
 */
public interface ActorDataProvider {

    /**
     * Gets the acting character by given skin name
     *
     * @param skinName - the name of skin for character
     * @return the {@link ActingCharacterModel}
     */
    ActingCharacterModel getActingCharacter(String skinName);

}
