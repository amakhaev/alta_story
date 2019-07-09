package com.alta.behaviorprocess.service.quest;

import com.alta.behaviorprocess.core.DataStorage;
import com.alta.behaviorprocess.data.quest.QuestRepository;
import com.alta.behaviorprocess.service.Behavior;
import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.behaviorprocess.shared.scenario.ScenarioImpl;
import com.alta.behaviorprocess.shared.scenario.Scenario;
import com.alta.behaviorprocess.shared.scenario.ScenarioFactory;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectListener;
import com.alta.behaviorprocess.sync.DataSynchronizer;
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
    private final QuestRepository questRepository;
    private final DataSynchronizer dataSynchronizer;

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
        scenario.subscribeToResult(
                () -> this.onScenarioCompleted(mainQuest.getName(), mainQuest.getCurrentStep().getStepNumber()),
                () -> this.onScenarioFailed(mainQuest.getName(), mainQuest.getCurrentStep().getStepNumber())
        );
        return scenario;
    }

    private void onScenarioCompleted(String questName, int stepNumber) {
        this.questRepository.completeQuestStep(questName, stepNumber);
        this.dataSynchronizer.synchronizeQuests();
        log.debug("The step {} in quest {} was completed", stepNumber, questName);
    }

    private void onScenarioFailed(String questName, int stepNumber) {
        log.warn("Execution of step {} in quest {} was failed.", stepNumber, questName);
    }
}
