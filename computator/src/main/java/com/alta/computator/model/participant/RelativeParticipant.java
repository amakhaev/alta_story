package com.alta.computator.model.participant;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the participant that depends on coordinates another participant
 */
public class RelativeParticipant extends ParticipantComputation {

    @Setter
    @Getter
    private Point currentGlobalCoordinates;

    /**
     * Initialize new instance of {@link RelativeParticipant}
     *
     * @param uuid - the UUID of participant
     */
    public RelativeParticipant(String uuid) {
        super(uuid);
    }
}
