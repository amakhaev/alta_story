package com.alta.behaviorprocess.shared.scenario;

import com.alta.behaviorprocess.data.effect.EffectModel;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.Effect;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectFactory;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectListener;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * Provides the scenario of effects.
 */
@Slf4j
public class ScenarioImpl implements Scenario {

    private final EffectListener effectListener;
    private final EffectFactory effectFactory;
    private final String targetedParticipantUuid;

    private Queue<EffectModel> currentScenario;
    private Effect currentEffect;
    private Runnable successCallback;
    private Runnable failCallback;

    @Getter
    private boolean completed;

    /**
     * Initialize new instance of {@link ScenarioImpl}.
     */
    @AssistedInject
    public ScenarioImpl(EffectFactory effectFactory,
                        @Assisted @NonNull EffectListener effectListener,
                        @Assisted("targetedParticipantUuid") @NonNull String targetedParticipantUuid,
                        @Assisted @NonNull List<EffectModel> effects) {
        this.effectListener = effectListener;
        this.effectFactory = effectFactory;
        this.targetedParticipantUuid = targetedParticipantUuid;
        this.currentScenario = new ArrayDeque<>();
        this.currentScenario.addAll(effects);
    }

    /**
     * Executes of scenario.
     */
    @Override
    public void execute() {
        if (this.currentScenario.isEmpty()) {
            this.markAsFailedCompleted();
            return;
        }

        if (this.currentEffect != null) {
            throw new RuntimeException("ScenarioImpl already in progress. Can't add another effects.");
        }

        this.determinateScenarioAndStart();
    }

    /**
     * Run the next effect if needed.
     */
    @Override
    public void runNextEffect() {
        if (this.currentEffect != null) {
            this.currentEffect.triggerNext();
        }
    }

    /**
     * Subscribes the sender to result of scenario execution.
     *
     * @param success - the callback that is invoking if scenario completed successfully.
     * @param fail    - the callback that is invoking if scenario fail.
     */
    public void subscribeToResult(Runnable success, Runnable fail) {
        this.successCallback = success;
        this.failCallback = fail;
    }

    private void determinateScenarioAndStart() {
        if (this.currentEffect != null) {
            log.warn(
                    "{} effect already in progress. No any action will be performed",
                    this.currentEffect.getClass().getSimpleName()
            );
            return;
        }

        if (this.currentScenario.peek() == null) {
            log.debug(
                    "The effect.scenario effect completed. Target uuid: {}",
                    this.targetedParticipantUuid
            );
            this.markAsSuccessfulCompleted();
            return;
        }

        Effect effect = null;
        EffectModel effectModel = this.currentScenario.poll();
        switch (effectModel.getType()) {
            case DIALOGUE:
                effect = this.effectFactory.createDialogueEffect(this.targetedParticipantUuid, this.effectListener);
                break;
            case HIDE_FACILITY:
                effect = this.effectFactory.createHideFacilityEffect(this.effectListener);
                break;
            case SHOW_FACILITY:
                effect = this.effectFactory.createShowFacilityEffect(this.effectListener);
                break;
            default:
                log.error("Unknown type of effect: {}", effectModel.getType());
                this.markAsFailedCompleted();
        }

        if (effect != null) {
            this.startEffect(effectModel, effect);
        }
    }

    private void startEffect(EffectModel effectModel, Effect effect) {
        this.currentEffect = effect;
        this.currentEffect.setCompleteCallback(() -> {
            this.currentEffect = null;
            this.determinateScenarioAndStart();
        });
        this.currentEffect.start(effectModel);
    }

    private void markAsSuccessfulCompleted() {
        if (this.successCallback != null) {
            this.successCallback.run();
        }
        this.completed = true;
    }

    private void markAsFailedCompleted() {
        if (this.failCallback != null) {
            this.failCallback.run();
        }
        this.completed = true;
    }
}
