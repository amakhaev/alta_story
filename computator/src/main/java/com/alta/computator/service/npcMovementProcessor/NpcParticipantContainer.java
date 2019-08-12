package com.alta.computator.service.npcMovementProcessor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides the container to store npcMovementProcessor participants.
 */
class NpcParticipantContainer<T> {


    private Map<String, T> initializedParticipants;
    private Map<String, T> notInitializedParticipants;

    /**
     * Initialize new instance of {@link NpcParticipantContainer}.
     */
    NpcParticipantContainer() {
        this.initializedParticipants = new HashMap<>();
        this.notInitializedParticipants = new HashMap<>();
    }

    /**
     * Indicates when not initialized list is not empty.
     *
     * @return true if count of not initialized items > 0, false otherwise.
     */
    boolean hasNotInitializedParticipants() {
        return !this.notInitializedParticipants.isEmpty();
    }

    /**
     * Indicates when initialized participant is not empty.
     *
     * @return true if count of initialized participants > 0, false otherwise.
     */
    boolean hasInitializedParticipants() {
        return !this.initializedParticipants.isEmpty();
    }

    /**
     * Gets the collection of not initialized participants.
     *
     * @return the {@link Collections} of participants.
     */
    Collection<T> getNotInitializedParticipants() {
        return this.notInitializedParticipants.values();
    }

    /**
     * Gets the collection of initialized participants.
     *
     * @return the {@link Collections} of participants.
     */
    Collection<T> getInitializedParticipants() {
        return this.initializedParticipants.values();
    }

    /**
     * Add participant into container and mark it as not initialized.
     *
     * @param value - the npcMovementProcessor participant to be added.
     */
    void addParticipant(String uuid, T value) {
        this.notInitializedParticipants.put(uuid, value);
    }

    /**
     * Marks all not initialized participants as initialized.
     */
    void markAllAsInitialized() {
        this.initializedParticipants.putAll(this.notInitializedParticipants);
        this.notInitializedParticipants.clear();
    }

    /**
     * Gets the participant by given UUID.
     *
     * @param uuid - the UUID of participant to be found.
     * @return the T instance.
     */
    T getParticipant(String uuid) {
        if (this.notInitializedParticipants.containsKey(uuid)) {
            return this.notInitializedParticipants.get(uuid);
        } else if (this.initializedParticipants.containsKey(uuid)) {
            return this.initializedParticipants.get(uuid);
        }

        return null;
    }

    /**
     * Indicates when container stored participant with given UUID.
     *
     * @param uuid - the uuid of participant.
     * @return true if participant with given UUID is stored in the container, false otherwise.
     */
    boolean hasParticipant(String uuid) {
        return this.notInitializedParticipants.containsKey(uuid) || this.initializedParticipants.containsKey(uuid);
    }
}
