package com.alta.computator.service.movement.actor;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.actor.SimpleNpcParticipant;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides the calculations for list of simple NPC
 */
@Slf4j
public class SimpleNpcListComputator {

    private Map<String, SimpleNpcComputator> simpleNpcComputators;

    /**
     * Initialize new instance of {@link SimpleNpcListComputator}
     */
    public SimpleNpcListComputator() {
        this.simpleNpcComputators = new HashMap<>();
    }

    /**
     * Adds the simple npc participant for computation
     *
     * @param npcParticipant - the npc for which calculation should be applied
     */
    public void add(SimpleNpcParticipant npcParticipant) {
        if (npcParticipant == null) {
            log.warn("Null reference to npc participant");
            return;
        }

        this.simpleNpcComputators.put(npcParticipant.getUuid(), new SimpleNpcComputator(npcParticipant));
    }

    /**
     * Handles the computing of coordinates for simple npc participants
     *
     * @param altitudeMap - the altitude map
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     * @param delta - the time between last and previous one calls
     */
    public void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates, int delta) {
        if (this.simpleNpcComputators.isEmpty()) {
            return;
        }

        this.simpleNpcComputators.forEach((key, value) -> value.onCompute(altitudeMap, focusPointGlobalCoordinates, delta));
    }

    /**
     * Gets the simple npc participant by given UUID
     *
     * @param uuid - the UUID of participant
     * @return the {@link SimpleNpcParticipant} instance of null if key not present
     */
    public SimpleNpcParticipant getSimpleNpcParticipant(String uuid) {
        return this.simpleNpcComputators.containsKey(uuid) ?
                this.simpleNpcComputators.get(uuid).getSimpleNpcParticipant() : null;
    }
}
