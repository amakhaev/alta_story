package com.alta.computator.calculator.facility;

import com.alta.computator.calculator.MovementUpdater;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.core.storage.StorageReader;
import com.google.common.collect.Sets;

import java.awt.*;
import java.util.Set;

/**
 * Provides the implementation of movement calculator that applied to facilities only.
 */
public class FacilityMediatorImpl implements FacilityMediator, MovementUpdater {

    private final StorageReader storageReader;
    private final Set<String> initializedFacilities;

    /**
     * Initialize new instance of {@link FacilityMediatorImpl}.
     *
     * @param storageReader - the {@link StorageReader} instance.
     */
    public FacilityMediatorImpl(StorageReader storageReader) {
        this.storageReader = storageReader;
        this.initializedFacilities = Sets.newHashSet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(int delta) {
        this.storageReader.getFacilities().forEach(facility -> {
            this.makeInitializationForNew(facility);
            FacilityCalculator.updateFacilityCoordinates(
                    facility,
                    this.storageReader.getAltitudeMap(),
                    this.storageReader.getFocusPoint().getCurrentGlobalCoordinates()
            );
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TargetedParticipantSummary findFacilityTargetByMapCoordinates(Point mapCoordinates) {
        return this.storageReader.getFacilities().stream()
                .filter(facilityParticipant -> facilityParticipant.getShiftCoordinatesByMapCoordinates(mapCoordinates) != null)
                .findFirst()
                .map(facilityParticipant -> new TargetedParticipantSummary(
                        facilityParticipant.getUuid(),
                        mapCoordinates,
                        facilityParticipant.getParticipantType(),
                        facilityParticipant.getShiftCoordinatesByMapCoordinates(mapCoordinates)
                ))
                .orElse(null);
    }

    private void makeInitializationForNew(FacilityParticipant participant) {
        if (this.initializedFacilities.contains(participant.getUuid())) {
            return;
        }

        FacilityCalculator.initialize(this.storageReader.getAltitudeMap(), participant);
        this.initializedFacilities.add(participant.getUuid());
    }
}
