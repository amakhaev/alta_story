package com.alta.computator.model.participant;

import lombok.Getter;

/**
 * Provides the participant of computation. Contains helper information related to computation.
 */
public abstract class ParticipantComputation {

    @Getter
    private final String uuid;

    @Getter
    private final ParticipatType participantType;

    /**
     * Initialize new instance of {@link ParticipantComputation}
     *
     * @param uuid - the UUID of participant
     * @param participantType - the type of participant
     */
    protected ParticipantComputation(String uuid, ParticipatType participantType) {
        this.uuid = uuid;
        this.participantType = participantType;
    }
}
