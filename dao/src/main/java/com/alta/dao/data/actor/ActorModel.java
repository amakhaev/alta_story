package com.alta.dao.data.actor;

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

    private final String pathToTileSetImage;
    private final String pathToFaceSetImage;
    private final int zIndex;
    private final int durationTime;
    private final ActorFaceSetDescriptorModel faceSetDescriptor;
    private final ActorTileSetDescriptorModel tileSetDescriptor;

    @Setter
    private Point startMapCoordinates;

    @Setter
    private String uuid;

    @Setter
    private int repeatingMovementDurationTime;
}
