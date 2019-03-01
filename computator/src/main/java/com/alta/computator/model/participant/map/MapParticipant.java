package com.alta.computator.model.participant.map;

import com.alta.computator.model.participant.ParticipantComputation;
import com.alta.computator.model.participant.ParticipatType;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the participant that depends on coordinates another participant
 */
public class MapParticipant extends ParticipantComputation {

    @Getter
    private Point currentGlobalCoordinates;

    /**
     * Initialize new instance of {@link MapParticipant}
     *
     * @param uuid - the UUID of participant
     */
    public MapParticipant(String uuid) {
        super(uuid, ParticipatType.MAP);
        this.currentGlobalCoordinates = new Point(0, 0);
    }

    /**
     * Updates the value of current global coordinates
     *
     * @param x - the X coordinate
     * @param y - the Y coordinate
     */
    public void updateCurrentGlobalCoordinates(int x, int y) {
        this.currentGlobalCoordinates.x = x;
        this.currentGlobalCoordinates.y = y;
    }
}
