package com.alta.computator.core.storage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.model.participant.map.MapParticipant;

import java.util.List;

/**
 * Provides the read functionality for the data storage.
 */
public interface StorageReader {

    /**
     * Gets the altitude map from storage.
     *
     * @return the {@link AltitudeMap} instance.
     */
    AltitudeMap getAltitudeMap();

    /**
     * Gets the focus pont participant from storage.
     *
     * @return the {@link FocusPointParticipant} instance from storage.
     */
    FocusPointParticipant getFocusPoint();

    /**
     * Gets the map participant from storage.
     *
     * @return the {@link com.alta.computator.model.participant.map.MapParticipant} instance.
     */
    MapParticipant getMap();

    /**
     * Gets the acting character from storage.
     *
     * @return the {@link ActingCharacterParticipant} instance.
     */
    ActingCharacterParticipant getActingCharacter();

    /**
     * Gets the list of facilities.
     *
     * @return the {@link List} of facility participants.
     */
    List<FacilityParticipant> getFacilities();

    /**
     * Finds the facility participant by given UUID.
     *
     * @param uuid - the uuid of facility.
     * @return the {@link FacilityParticipant} instance.
     */
    FacilityParticipant findFacilityByUuid(String uuid);

    /**
     * Gets the layer of sorted participants.
     *
     * @return the {@link List} of participant.
     */
    List<CoordinatedParticipant> getSortedParticipants();

    /**
     * Gets available NPC participants from storage.
     *
     * @return the {@link List} of NPC participants.
     */
    List<NpcParticipant> getNpcParticipants();

}
