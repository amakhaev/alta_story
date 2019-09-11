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
 * Provides the create and/or update functionality for the data storage.
 */
public interface StorageWriter {

    /**
     * Writes the altitude map into storage.
     *
     * @param altitudeMap - the value to be written.
     */
    void addAltitudeMap(AltitudeMap altitudeMap);

    /**
     * Writes the focus point participant into storage.
     *
     * @param focusPointParticipant - the {@link FocusPointParticipant} instance to be written.
     */
    void addFocusPoint(FocusPointParticipant focusPointParticipant);

    /**
     * Writes the map participant into storage.
     *
     * @param mapParticipant - the value to be written.
     */
    void addMap(MapParticipant mapParticipant);

    /**
     * Writes the facility participant into storage.
     *
     * @param facilityParticipant - the value to be written.
     */
    void addFacility(FacilityParticipant facilityParticipant);

    /**
     * Writes the acting character participant into storage.
     *
     * @param actingCharacterParticipant - the value to be written.
     */
    void addActingCharacter(ActingCharacterParticipant actingCharacterParticipant);

    /**
     * Writes the NPC participant into storage.
     *
     * @param npcParticipant - the value to be written.
     */
    void addNpcParticipant(NpcParticipant npcParticipant);

    /**
     * Writes the participants into sorted layer by z-index.
     *
     * @param participants - the list of participant to be added.
     */
    void addParticipantsToLayer(List<? extends CoordinatedParticipant> participants);

    /**
     * Writes the participant into sorted layer by z-index.
     *
     * @param participant - the list of participant to be added.
     */
    void addParticipantToLayer(CoordinatedParticipant participant);

    /**
     * Removes the facility from storage.
     *
     * @param facilityUuid - the uuid of facility to be removed.
     */
    void removeFacility(String facilityUuid);

    /**
     * Removes the facility from layer.
     *
     * @param participantUuid - the uuid of facility to be removed.
     */
    void removeParticipantFromLayer(String participantUuid);

    /**
     * Clears all data in the computator.
     */
    void clearAll();
}
