package com.alta.behaviorprocess.shared.scenario;

import com.alta.behaviorprocess.data.effect.EffectModel;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectListener;
import com.google.inject.assistedinject.Assisted;
import lombok.NonNull;

import java.util.List;

/**
 * Provides the factory related to interaction interaction.scenario.
 */
public interface ScenarioFactory {

    /**
     * Creates new instance of {@link ScenarioImpl}.
     *
     * @param effectListener - the {@link EffectListener} instance.
     * @param targetedParticipantUuid - the uuid of target participant.
     * @param effects - the effects to be invoked..
     * @return create {@link Scenario} instance.
     */
    ScenarioImpl createScenario(@Assisted @NonNull EffectListener effectListener,
                                @Assisted("targetedParticipantUuid") @NonNull String targetedParticipantUuid,
                                @Assisted @NonNull List<EffectModel> effects);

}
