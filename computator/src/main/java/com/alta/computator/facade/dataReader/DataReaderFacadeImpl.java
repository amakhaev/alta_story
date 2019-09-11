package com.alta.computator.facade.dataReader;

import com.alta.computator.calculator.actingCharacter.ActingCharacterMediator;
import com.alta.computator.calculator.facility.FacilityMediator;
import com.alta.computator.calculator.map.MapMediator;
import com.alta.computator.calculator.npc.NpcMediator;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.core.storage.StorageReader;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.List;

/**
 * Provides the facade that control read process.
 */
@RequiredArgsConstructor
public class DataReaderFacadeImpl implements DataReaderFacade {

    private final MapMediator mapMediator;
    private final StorageReader storageReader;
    private final NpcMediator npcMediator;
    private final ActingCharacterMediator actingCharacterMediator;
    private final FacilityMediator facilityMediator;

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getMapGlobalCoordinates() {
        return this.mapMediator.getMapGlobalCoordinates();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CoordinatedParticipant> getSortedParticipants() {
        return this.storageReader.getSortedParticipants();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActorParticipant findActorByUuid(String uuid) {
        if (this.storageReader.getActingCharacter() != null && this.storageReader.getActingCharacter().getUuid().equals(uuid)) {
            return this.storageReader.getActingCharacter();
        }

        return this.npcMediator.getParticipant(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TargetedParticipantSummary findParticipantTargetedByActingCharacter() {
        Point targetParticipantMapCoordinates = this.actingCharacterMediator.getMapCoordinatesOfTargetParticipant();
        TargetedParticipantSummary summary = this.npcMediator.findNpcTargetByMapCoordinates(targetParticipantMapCoordinates);
        if (summary != null) {
            return summary;
        }

        return this.facilityMediator.findFacilityTargetByMapCoordinates(targetParticipantMapCoordinates);
    }
}
