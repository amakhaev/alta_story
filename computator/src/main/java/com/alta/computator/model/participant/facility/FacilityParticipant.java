package com.alta.computator.model.participant.facility;

import com.alta.computator.model.participant.ParticipantComputation;
import com.alta.computator.model.participant.ParticipatType;
import lombok.Getter;

import java.awt.*;
import java.util.List;


/**
 * Provides the participant that provides calculations related to facility
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
     * Updates the value of start global coordinates
     *
     * @param x - the X coordinate
     * @param y - the Y coordinate
     */
    public void updateStartGlobalCoordinates(int x, int y) {
        this.startGlobalCoordinates.x = x;
        this.startGlobalCoordinates.y = y;
    }
}
