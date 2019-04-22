package com.alta.computator.service.layer;

import com.alta.computator.model.participant.CoordinatedParticipant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the calculations for layers
 */
@Slf4j
public class LayerComputator {

    @Getter
    private List<CoordinatedParticipant> sortedParticipants;

    /**
     * Initialize new instance of {@link LayerComputator}
     */
    public LayerComputator() {
        this.sortedParticipants = new ArrayList<>();
    }

    /**
     * Adds the participants to calculate positions in the layer
     *
     * @param participants - the participants that used in layer
     */
    public void addParticipants(List<? extends CoordinatedParticipant> participants) {
        if (participants == null || participants.isEmpty()) {
            log.warn("Participant list is empty. No any new items in layers.");
            return;
        }

        this.sortedParticipants.addAll(participants);
        this.resortParticipants();
    }

    /**
     * Adds the participant to calculable position in the layer
     *
     * @param participant - the participant tha used in layer
     */
    public void addParticipant(CoordinatedParticipant participant) {
        if (participant == null) {
            log.warn("Participant is null");
            return;
        }

        this.sortedParticipants.add(participant);
        this.resortParticipants();
    }

    private void resortParticipants() {
        this.sortedParticipants = this.sortedParticipants.stream()
                .sorted(Comparator.comparingInt(CoordinatedParticipant::getZIndex))
                .collect(Collectors.toList());
    }
}
