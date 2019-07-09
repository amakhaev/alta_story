package com.alta.mediator.domain.quest;

import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.behaviorprocess.data.quest.QuestStepModel;
import com.alta.behaviorprocess.data.quest.QuestStepTriggerType;
import com.alta.dao.data.quest.QuestListItemModel;
import com.alta.dao.domain.quest.QuestListService;
import com.alta.dao.domain.quest.QuestService;
import com.alta.mediator.domain.effect.EffectDataProvider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Describes the provider of model related to interactions.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class QuestDataProviderImpl implements QuestDataProvider {

    private final QuestService questService;
    private final QuestListService questListService;
    private final EffectDataProvider effectDataProvider;

    /**
     * Gets the quest model.
     *
     * @param questName         - the name of quest to be retrieved.
     * @param currentStepNumber - the number of current step to be retrieved.
     * @return the {@link QuestModel} instance.
     */
    @Override
    public QuestModel getQuestModel(String questName, int currentStepNumber) {
        QuestListItemModel mainQuestListItem = this.questListService.findQuestByName(questName);
        com.alta.dao.data.quest.QuestModel questModel = this.questService.getQuestWithSpecifiedSteps(
                mainQuestListItem.getPathToDescriptor(), currentStepNumber
        );

        return this.createQuestModel(questModel);
    }

    /**
     * Gets the count of steps that available in quest.
     *
     * @param questName - the name of quest to search steps.
     * @return the count of steps.
     */
    @Override
    public int getCountOfStepsInQuest(String questName) {
        QuestListItemModel mainQuestListItem = this.questListService.findQuestByName(questName);
        if (mainQuestListItem == null) {
            log.error("Quest with given name {} not found", questName);
            return 0;
        }

        return mainQuestListItem.getStepCount();
    }

    private QuestModel createQuestModel(com.alta.dao.data.quest.QuestModel questModel) {
        return QuestModel.builder()
                .name(questModel.getName())
                .displayName(questModel.getDisplayName())
                .currentStep(questModel.getSteps() == null || questModel.getSteps().isEmpty() ?
                        null : this.createQuestStep(questModel.getSteps().get(0))
                )
                .build();
    }

    private QuestStepModel createQuestStep(com.alta.dao.data.quest.QuestStepModel questStepModel) {
        return QuestStepModel.builder()
                .stepNumber(questStepModel.getStepNumber())
                .triggerType(QuestStepTriggerType.valueOf(questStepModel.getTriggerType()))
                .triggerMap(questStepModel.getTriggerMap())
                .targetUuid(questStepModel.getTargetUuid())
                .chapterIndicatorFrom(questStepModel.getChapterIndicatorFrom())
                .chapterIndicatorTo(questStepModel.getChapterIndicatorTo())
                .effects(this.effectDataProvider.getEffects(questStepModel.getEffects()))
                .build();
    }
}
