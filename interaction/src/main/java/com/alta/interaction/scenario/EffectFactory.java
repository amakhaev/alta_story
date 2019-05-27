package com.alta.interaction.scenario;

/**
 * Provides the factory to create interactions.
 */
public interface EffectFactory {

    /**
     * Creates the instance of {@link DialogueEffect}.
     */
    DialogueEffect createDialogueInteraction(String targetedParticipantUuid, EffectListener effectListener);

    /**
     * Creates tje instance of {@link HideFacilityEffect}.
     *
     * @return created {@link HideFacilityEffect} instance.
     */
    HideFacilityEffect createHideFacilityInteraction(EffectListener effectListener);

    /**
     * Creates tje instance of {@link ShowFacilityEffect}.
     *
     * @return created {@link ShowFacilityEffect} instance.
     */
    ShowFacilityEffect createShowFacilityInteraction(EffectListener effectListener);

}
