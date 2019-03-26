package com.alta.computator.model.participant.facility;

import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.ParticipatType;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the participant that describes one part of map
 */
public class FacilityPartParticipant extends CoordinatedParticipant {

    @Getter
    private final Point shiftTilePosition;

    @Getter
    private final Point spriteSheetTileMapCoordinate;

    @Getter
    private final TileState tileState;

    /**
     * Initialize new instance of {@link CoordinatedParticipant}
     * @param uuid - the UUID of participant
     * @param shiftTilePosition - the shift size between start coordinates and tile on image set
     */
    public FacilityPartParticipant(String uuid,
                                   int zIndex,
                                   Point startMapCoordinates,
                                   Point shiftTilePosition,
                                   Point spriteSheetTileMapCoordinate,
                                   TileState tileState) {
        super(uuid, startMapCoordinates, zIndex, ParticipatType.FACILITY_PART);
        this.shiftTilePosition = shiftTilePosition;
        this.spriteSheetTileMapCoordinate = spriteSheetTileMapCoordinate;
        this.tileState = tileState;
    }
}
