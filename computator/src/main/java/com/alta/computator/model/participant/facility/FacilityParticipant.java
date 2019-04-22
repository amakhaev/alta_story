package com.alta.computator.model.participant.facility;

import com.alta.computator.model.participant.ParticipantComputation;
import com.alta.computator.model.participant.ParticipatType;
import lombok.Getter;
import lombok.NonNull;

import java.awt.*;
import java.util.List;


/**
 * Provides the participant that provides calculations related to map
 */
public class FacilityParticipant extends ParticipantComputation {

    @Getter
    private final Point startMapCoordinates;
    @Getter
    private Point startGlobalCoordinates;

    @Getter
    private final List<FacilityPartParticipant> facilityPartParticipants;

    /**
     * Initialize new instance of {@link ParticipantComputation}
     *
     * @param uuid - the UUID of participant
     */
    public FacilityParticipant(String uuid, Point startMapCoordinates, List<FacilityPartParticipant> facilityPartParticipants) {
        super(uuid, ParticipatType.FACILITY);
        this.startMapCoordinates = startMapCoordinates;
        this.startGlobalCoordinates = new Point();
        this.facilityPartParticipants = facilityPartParticipants;
    }

    /**
     * Gets the shift coordinates by given map coordinates.
     *
     * @param mapCoordinate - the map coordinates.
     * @return the {@link Point} instance that describes shift or null if not found.
     */
    public Point getShiftCoordinatesByMapCoordinates(@NonNull Point mapCoordinate) {
        Point temp = new Point();
        return this.facilityPartParticipants.stream()
                .filter(part -> {
                            temp.x = part.getStartMapCoordinates().x + part.getShiftTilePosition().x;
                            temp.y = part.getStartMapCoordinates().y + part.getShiftTilePosition().y;
                            return temp.equals(mapCoordinate);
                        }
                )
                .findFirst()
                .map(FacilityPartParticipant::getShiftTilePosition)
                .orElse(null);
    }
}
