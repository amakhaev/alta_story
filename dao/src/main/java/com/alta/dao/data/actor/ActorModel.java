package com.alta.dao.data.actor;

import com.alta.dao.domain.actor.ActorEntity;
import com.alta.dao.domain.actor.TileSetDescriptorEntity;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.UUID;

/**
 * Provides the model that describes actor
 */
public final class ActorModel {

    private final ActorEntity actorEntity;

    @Getter
    private final TileSetDescriptorEntity descriptor;

    @Getter
    private final String pathToImageSet;

    @Getter
    private final Point startMapCoordinates;

    @Getter
    private final String uuid;

    @Getter
    @Setter
    private int repeatingMovementDurationTime;

    public ActorModel(ActorEntity actorEntity,
                      String pathToImageSet,
                      TileSetDescriptorEntity descriptor,
                      Point startMapCoordinates) {
        this.actorEntity = actorEntity;
        this.pathToImageSet = pathToImageSet;
        this.descriptor = descriptor;
        this.startMapCoordinates = startMapCoordinates;
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * Gets the z-index of actor
     */
    public int getZIndex() {
        return this.actorEntity.getZIndex();
    }

    /**
     * Gets the duration time of animation
     */
    public int getDurationTime() {
        return this.actorEntity.getDurationTime();
    }

    /**
     * Gets the name of skin.
     */
    public String getSkinName() {
        return this.actorEntity.getImageName();
    }
}
