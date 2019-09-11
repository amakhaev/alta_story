package com.alta.computator.core.storage;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.model.participant.map.MapParticipant;
import com.google.inject.Inject;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * Provides the implementation of storage reader.
 */
public class StorageReaderImpl implements StorageReader {

    private final ModelStorage modelStorage;
    private final ReadWriteLock readWriteLock;

    /**
     * Initialize new instance of {@link StorageReaderImpl}
     */
    @Inject
    public StorageReaderImpl(ModelStorage modelStorage) {
        this.modelStorage = modelStorage;
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AltitudeMap getAltitudeMap() {
        return this.read((v) -> this.modelStorage.getAltitudeMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FocusPointParticipant getFocusPoint() {
        return this.read((v) -> this.modelStorage.getFocusPointParticipant());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapParticipant getMap() {
        return this.read((v) -> this.modelStorage.getMapParticipant());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActingCharacterParticipant getActingCharacter() {
        return this.read((v) -> this.modelStorage.getActingCharacterParticipant());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FacilityParticipant> getFacilities() {
        return this.read((v) -> this.modelStorage.getFacilityParticipants());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FacilityParticipant findFacilityByUuid(String uuid) {
        return this.modelStorage.getFacilityParticipants().stream()
                .filter(participant -> participant.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CoordinatedParticipant> getSortedParticipants() {
        return this.read((v) -> this.modelStorage.getSortedParticipants());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NpcParticipant> getNpcParticipants() {
        return this.read((v) -> this.modelStorage.getNpcParticipants());
    }

    private <T> T read(Function<Void, T> readFunction) {
        this.readWriteLock.readLock().lock();
        try {
            return readFunction.apply(null);
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }

}
