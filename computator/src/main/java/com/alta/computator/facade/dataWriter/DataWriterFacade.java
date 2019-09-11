package com.alta.computator.facade.dataWriter;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;

import java.awt.*;
import java.util.List;

/**
 * Provides the facade that control write process.
 */
public interface DataWriterFacade {

    /**
     * Adds the altitude map into storage.
     *
     * @param altitudeMap - the value to be written.
     */
    void addAltitudeMap(AltitudeMap altitudeMap);

    /**
     * Adds the focus point participant into storage.
     *
     * @param mapStartPosition - the start position of focus point of map.
     */
    void addFocusPoint(Point mapStartPosition);

    /**
     * Adds the facility participants for calculate coordinates.
     *
     * @param facilityParticipants - the list of facility participants to be added.
     */
    void addFacilities(List<FacilityParticipant> facilityParticipants);

    /**
     * Adds the acting character participant into storage.
     *
     * @param actingCharacterParticipant - the value to be written.
     */
    void addActingCharacter(ActingCharacterParticipant actingCharacterParticipant);

    /**
     * Adds the simple npcMovement to stage for computation
     *
     * @param npcParticipants - the npcMovement participants to be added for computation.
     */
    void addNpcCharacters(List<NpcParticipant> npcParticipants);

    /**
     * Removes the facility from storage.
     *
     * @param facilityUuid - the uuid of facility to be removed.
     */
    void removeFacility(String facilityUuid);

    /**
     * Clears all data in the computator.
     */
    void clearAll();
}
