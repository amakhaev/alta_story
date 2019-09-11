package com.alta.computator.core.storage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.model.participant.map.MapParticipant;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Provides the implementation of CRUD operations for storage.
 */
@Slf4j
public class StorageWriterImpl implements StorageWriter {

    private final ModelStorage modelStorage;
    private final ReadWriteLock readWriteLock;

    /**
     * Initialize new instance of {@link StorageWriterImpl}.
     */
    public StorageWriterImpl(ModelStorage modelStorage) {
        this.modelStorage = modelStorage;
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAltitudeMap(AltitudeMap altitudeMap) {
        this.write(() -> this.modelStorage.setAltitudeMap(altitudeMap));
        log.info("Altitude map was added into computator storage.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFocusPoint(FocusPointParticipant focusPointParticipant) {
        this.write(() -> this.modelStorage.setFocusPointParticipant(focusPointParticipant));
        log.info("Focus point {} was added into computator storage.", focusPointParticipant.getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMap(MapParticipant mapParticipant) {
        this.write(() -> this.modelStorage.setMapParticipant(mapParticipant));
        log.info("Map participant {} was added into computator storage.", mapParticipant.getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFacility(FacilityParticipant facilityParticipant) {
        this.write(() -> this.modelStorage.getFacilityParticipants().add(facilityParticipant));
        log.debug("Facility participant {} was added into computator storage.", facilityParticipant.getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addActingCharacter(ActingCharacterParticipant actingCharacterParticipant) {
        this.write(() -> this.modelStorage.setActingCharacterParticipant(actingCharacterParticipant));
        log.info("Acting character participant {} was added into computator storage.", actingCharacterParticipant.getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNpcParticipant(NpcParticipant npcParticipant) {
        this.write(() -> this.modelStorage.getNpcParticipants().add(npcParticipant));
        log.debug("Npc participant {} was added into computator storage.", npcParticipant.getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParticipantsToLayer(List<? extends CoordinatedParticipant> participants) {
        this.write(() -> {
            this.modelStorage.getSortedParticipants().addAll(participants);
            this.modelStorage.sortParticipantByZIndex();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParticipantToLayer(CoordinatedParticipant participant) {
        this.write(() -> {
            this.modelStorage.getSortedParticipants().add(participant);
            this.modelStorage.sortParticipantByZIndex();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFacility(String facilityUuid) {
        this.write(() ->
                this.modelStorage.getFacilityParticipants().removeIf(participant -> participant.getUuid().equals(facilityUuid))
        );
        log.debug("Facility participant {} was removed from computator storage.", facilityUuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeParticipantFromLayer(String participantUuid) {
        this.write(() -> this.modelStorage.getSortedParticipants().removeIf(p -> p.getUuid().equals(participantUuid)));
        log.debug("Participant {} was removed from computator storage.", participantUuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearAll() {
        this.write(this.modelStorage::clear);
        log.debug("Storage cleared successfully.");
    }

    private void write(Runnable writeRunnable) {
        this.readWriteLock.writeLock().lock();
        try {
            writeRunnable.run();
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }
}
