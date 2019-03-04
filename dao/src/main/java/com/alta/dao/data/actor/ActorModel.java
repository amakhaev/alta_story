package com.alta.dao.data.actor;

import com.alta.dao.domain.actor.ActorEntity;
import com.alta.dao.domain.actor.TileSetDescriptorEntity;
import lombok.AllArgsConstructor;

/**
 * Provides the model that describes actor
 */
@AllArgsConstructor
public final class ActorModel {

    private final ActorEntity actorEntity;
    private final String pathToImageSet;
    private final TileSetDescriptorEntity descriptor;
}
