package com.alta.behaviorprocess.service.quest;

import com.alta.behaviorprocess.core.DataStorage;
import com.alta.behaviorprocess.service.Behavior;
import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.behaviorprocess.shared.scenario.ScenarioImpl;
import com.alta.behaviorprocess.shared.scenario.Scenario;
import com.alta.behaviorprocess.shared.scenario.ScenarioFactory;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectListener;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the behavior processor for main quest.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MainQuestBehavior implements Behavior<QuestScenarioData> {

    private final DataStorage dataStorage;
    private final ScenarioFactory scenarioFactory;
    private final EffectListener effectListener;

    /**
     * Gets the scenario by given params.
     *
     * @param scenarioParams - the params that applied to scenario.
     * @return created {@link Scenario} instance.
     */
    @Override
    public Scenario getScenario(QuestScenarioData scenarioParams) {
        QuestModel mainQuest = this.dataStorage.getMainQuest();
        if (mainQuest == null) {
            log.error("No main quest found in model storage");
            return null;
        }

        switch (mainQuest.getCurrentStep().getTriggerType()) {
            case DIALOG:
                return this.createScenarioForDialogTrigger(scenarioParams.getTargetUuid());
            default:
                log.error("Unknown trigger type for creating scenario: {}", mainQuest.getCurrentStep().getTriggerType());
        }

        return null;
    }

    private Scenario createScenarioForDialogTrigger(String targetUuid) {
        QuestModel mainQuest = this.dataStorage.getMainQuest();
        if (Strings.isNullOrEmpty(targetUuid) || !mainQuest.getCurrentStep().getTargetUuid().equals(targetUuid)) {
            return null;
        }


        ScenarioImpl scenario = this.scenarioFactory.createScenario(
                this.effectListener,
                targetUuid,
                mainQuest.getCurrentStep().getEffects()
        );
        scenario.subscribeToResult(this::onScenarioCompleted, this::onScenarioFailed);
        return scenario;
    }

    private void onScenarioCompleted() {
        log.debug("COMPLETED!!");
    }

    private void onScenarioFailed() {
        log.debug("FAILED!!!");
    }
}
