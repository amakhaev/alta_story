package com.alta.behaviorprocess.shared.scenario.senarioEffects;

/**
 * Provides the factory to create interactions.
 */
public interface EffectFactory {

    /**
     * Creates the instance of {@link DialogueEffect}.
     */
    DialogueEffect createDialogueEffect(String targetedParticipantUuid, EffectListener effectListener);

    /**
     * Creates the instance of {@link HideFacilityEffect}.
     *
     * @return created {@link HideFacilityEffect} instance.
     */
    HideFacilityEffect createHideFacilityEffect(EffectListener effectListener);

    /**
     * Creates the instance of {@link ShowFacilityEffect}.
     *
     * @return created {@link ShowFacilityEffect} instance.
     */
    ShowFacilityEffect createShowFacilityEffect(EffectListener effectListener);

    /**
     * Creates the instance of {@link ShowFacilityEffect}.
     *
     * @return created {@link ShowFacilityEffect} instance.
     */
    RouteMovementEffect createRouteMovementEffect(EffectListener effectListener);

}
