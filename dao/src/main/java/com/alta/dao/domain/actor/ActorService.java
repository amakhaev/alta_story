package com.alta.dao.domain.actor;

import com.alta.dao.data.actor.ActorModel;

import java.awt.*;

/**
 * Provides the service to make CRUD with actors
 */
public interface ActorService {

    /**
     * Gets the actor data by given file name of tile sets.
     *
     * @param name - the name of actor.
     *
     * @return the {@link ActorModel} instance.
     */
    ActorModel getActorModel(String name);

}
