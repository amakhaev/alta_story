package com.alta.computator.service.participantComputator.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.model.participant.actor.NpcParticipant;
import com.alta.computator.model.participant.actor.RouteNpcParticipant;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides the calculations for list of simple NPC
 */
@Slf4j
public class NpcListComputator {

    private Map<String, NpcComputator> npcComputators;

    /**
     * Initialize new instance of {@link NpcListComputator}
     */
    public NpcListComputator() {
        this.npcComputators = new HashMap<>();
    }

    /**
     * Adds the npc participant for computation
     *
     * @param npcParticipant - the npc for which calculation should be applied
     */
    public void add(NpcParticipant npcParticipant) {
        if (npcParticipant == null) {
            log.warn("Null reference to npc participant");
            return;
        }

        if (npcParticipant.getParticipantType() == ParticipatType.SIMPLE_NPC) {
            this.npcComputators.put(npcParticipant.getUuid(), new SimpleNpcComputator((SimpleNpcParticipant) npcParticipant));
        } else if (npcParticipant.getParticipantType() == ParticipatType.ROUTE_NPC) {
            this.npcComputators.put(npcParticipant.getUuid(), new RouteNpcComputator((RouteNpcParticipant) npcParticipant));
        }
    }

    /**
     * Handles the computing of coordinates for simple npc participants
     *
     * @param altitudeMap - the altitude map
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     * @param delta - the time between last and previous one calls
     */
    public void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        if (this.npcComputators.isEmpty()) {
            return;
        }

        this.npcComputators.forEach((key, value) -> value.onCompute(altitudeMap, focusPointGlobalCoordinates, delta));
    }

    /**
     * Gets the npc participant by given UUID
     *
     * @param uuid - the UUID of participant
     * @return the {@link SimpleNpcParticipant} instance of null if key not present
     */
    public NpcParticipant getSimpleNpcParticipant(String uuid) {
        return this.npcComputators.containsKey(uuid) ? this.npcComputators.get(uuid).getNpcParticipant() : null;
    }

    /**
     * Sets the pause on participantComputator process of NPC.
     *
     * @param isPause - indicates when calculation should be paused.
     */
    public void setPause(boolean isPause) {
        if (this.npcComputators != null) {
            this.npcComputators.values().forEach(
                    (simpleNpcComputator) -> simpleNpcComputator.setComputationPause(isPause)
            );
        }
    }

    /**
     * Sets the pause on participantComputator process for specific NPC.
     *
     * @param isPause   - indicates when calculation should be paused.
     * @param uuid      - the uuid of NPC to be paused
     */
    public void setPause(boolean isPause, String uuid) {
        if (this.npcComputators != null && this.npcComputators.containsKey(uuid)) {
            this.npcComputators.get(uuid).setComputationPause(isPause);
        }
    }

    /**
     * Finds the NPC that has given map coordinates.
     *
     * @param mapCoordinates - the map coordinates for searching.
     * @return the {@link TargetedParticipantSummary} instance of null if not found.
     */
    public TargetedParticipantSummary findNpcTargetByMapCoordinates(@NonNull Point mapCoordinates) {
        if (this.npcComputators.size() == 0) {
            return null;
        }

        return this.npcComputators.values().stream()
                .filter(c -> c.getNpcParticipant().getCurrentMapCoordinates().equals(mapCoordinates))
                .findFirst()
                .map(c -> new TargetedParticipantSummary(
                        c.getNpcParticipant().getUuid(),
                        c.getNpcParticipant().getCurrentMapCoordinates(),
                        c.getNpcParticipant().getParticipantType()
                ))
                .orElse(null);
    }

    /**
     * Gets the list of npc participats.
     *
     * @return the {@link java.util.List} of {@link SimpleNpcParticipant}.
     */
    public java.util.List<NpcParticipant> getNpcList() {
        return this.npcComputators.values().stream()
                .map(NpcComputator::getNpcParticipant)
                .collect(Collectors.toList());
    }
}
