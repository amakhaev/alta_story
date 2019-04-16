package com.alta.dao.data.actor;

import com.alta.dao.domain.actor.TileSetDescriptorEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the model that describes actor
 */
@Getter
@Builder
public final class ActorModel {

    private final TileSetDescriptorEntity descriptor;
    private final String pathToImageSet;
    private final int zIndex;
    private final int durationTime;

    @Setter
    private Point startMapCoordinates;

    @Setter
    private String uuid;

    @Setter
    private int repeatingMovementDurationTime;
}
