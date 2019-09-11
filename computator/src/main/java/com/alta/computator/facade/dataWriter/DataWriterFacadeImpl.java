package com.alta.computator.facade.dataWriter;

import com.alta.computator.calculator.CalculatorCache;
import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.model.participant.map.MapParticipant;
import com.alta.computator.core.storage.StorageReader;
import com.alta.computator.core.storage.StorageWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;
import java.util.UUID;

/**
 * Provides the facade that control write process.
 */
@Slf4j
@RequiredArgsConstructor
public class DataWriterFacadeImpl implements DataWriterFacade {

    private final StorageWriter storageWriter;
    private final StorageReader storageReader;
    private final CalculatorCache calculatorCache;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAltitudeMap(AltitudeMap altitudeMap) {
        this.storageWriter.addAltitudeMap(altitudeMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFocusPoint(Point mapStartPosition) {
        FocusPointParticipant focusPointParticipant = new FocusPointParticipant(mapStartPosition, UUID.randomUUID().toString());
        focusPointParticipant.updateCurrentMapCoordinates(mapStartPosition.x, mapStartPosition.y);
        this.storageWriter.addFocusPoint(focusPointParticipant);
        log.info("Added focus point to stage with UUID: {}", focusPointParticipant.getUuid());

        MapParticipant mapParticipant = new MapParticipant(UUID.randomUUID().toString());
        this.storageWriter.addMap(mapParticipant);
        log.info("Added map participant to stage with UUID: {}", mapParticipant.getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFacilities(List<FacilityParticipant> facilityParticipants) {
        if (facilityParticipants == null || facilityParticipants.isEmpty()) {
            log.warn("Attempt to add empty facility list.");
            return;
        }

        facilityParticipants.forEach(participant -> {
            this.storageWriter.addFacility(participant);
            this.storageWriter.addParticipantsToLayer(participant.getFacilityPartParticipants());
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addActingCharacter(ActingCharacterParticipant actingCharacterParticipant) {
        if (actingCharacterParticipant == null) {
            log.warn("Attempt to add empty acting character.");
            return;
        }

        this.storageWriter.addActingCharacter(actingCharacterParticipant);
        this.storageWriter.addParticipantToLayer(actingCharacterParticipant);
        log.info("Added acting character to stage with UUID: {}", actingCharacterParticipant.getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNpcCharacters(List<NpcParticipant> npcParticipants) {
        if (npcParticipants == null || npcParticipants.isEmpty()) {
            log.warn("Attempt to add empty NPC characters.");
            return;
        }

        npcParticipants.forEach(npcParticipant -> {
            this.storageWriter.addNpcParticipant(npcParticipant);
            this.storageWriter.addParticipantToLayer(npcParticipant);
            log.debug("Added simple npcMovement character to stage with UUID: {}.", npcParticipant.getUuid());
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFacility(String facilityUuid) {
        FacilityParticipant facilityParticipant = this.storageReader.findFacilityByUuid(facilityUuid);
        if (facilityParticipant == null || this.storageReader.getAltitudeMap() == null) {
            log.warn("Can't remove facility from computation. Facility with give uuid {} not found or altitude map is null.", facilityUuid);
            return;
        }

        facilityParticipant.getFacilityPartParticipants().forEach(participantPart -> {
            this.storageWriter.removeParticipantFromLayer(participantPart.getUuid());
            this.storageReader.getAltitudeMap().setTileState(
                    participantPart.getCurrentMapCoordinates().x,
                    participantPart.getCurrentMapCoordinates().y,
                    TileState.FREE
            );
        });
        this.storageWriter.removeFacility(facilityUuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearAll() {
        this.calculatorCache.clear();
        this.storageWriter.clearAll();
        log.info("Data from computator was cleared.");
    }
}
