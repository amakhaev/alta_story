package com.alta.computator.model.participant;

import lombok.Getter;

/**
 * Provides the participant of computation. Contains helper information related to computation.
 */
abstract class ParticipantComputation {

    @Getter
    private final String uuid;

    /**
     * Initialize new instance of {@link ParticipantComputation}
     *
     * @param uuid - the UUID of participant
     */
    ParticipantComputation(String uuid) {
        this.uuid = uuid;
    }
}
