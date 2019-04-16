package com.alta.dao.domain.actor;

import com.alta.dao.data.actor.ActorModel;

import java.awt.*;

/**
 * Provides the service to make CRUD with actors
 */
public interface ActorService {

    /**
     * Gets the actor model by given file name of tile sets.
     *
     * @param descriptorFileName - the full name of descriptor file like "file.dscr".
     *
     * @return the {@link ActorModel} instance.
     */
    ActorModel getActorModel(String descriptorFileName);

}
