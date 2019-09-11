package com.alta.computator.core.storage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.model.participant.map.MapParticipant;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides the storage of models that available for computator.
 */
public class ModelStorage {

    @Getter
    @Setter
    private AltitudeMap altitudeMap;

    @Getter
    @Setter
    private FocusPointParticipant focusPointParticipant;

    @Getter
    @Setter
    private MapParticipant mapParticipant;

    @Getter
    @Setter
    private ActingCharacterParticipant actingCharacterParticipant;

    @Getter
    private Map<String, CoordinatedParticipant> coordinatedParticipants;

    @Getter
    private List<CoordinatedParticipant> sortedParticipants;

    @Getter
    private List<FacilityParticipant> facilityParticipants;

    @Getter
    private List<NpcParticipant> npcParticipants;

    /**
     * Initialize new instance of {@link ModelStorage}.
     */
    public ModelStorage() {
        this.coordinatedParticipants = Maps.newConcurrentMap();
        this.sortedParticipants = Collections.synchronizedList(new ArrayList<>());
        this.facilityParticipants = Collections.synchronizedList(new ArrayList<>());
        this.npcParticipants = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Sorts the participants into list by zIndex.
     */
    void sortParticipantByZIndex() {
        this.sortedParticipants = this.sortedParticipants.stream()
                .sorted(Comparator.comparingInt(CoordinatedParticipant::getZIndex))
                .collect(Collectors.toList());
    }

    /**
     * Clears all data from storage.
     */
    void clear() {
        this.setAltitudeMap(null);
        this.setFocusPointParticipant(null);
        this.setMapParticipant(null);
        this.setActingCharacterParticipant(null);

        this.coordinatedParticipants.clear();
        this.sortedParticipants.clear();
        this.facilityParticipants.clear();
        this.npcParticipants.clear();
    }
}
