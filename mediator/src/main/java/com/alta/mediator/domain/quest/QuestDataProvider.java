package com.alta.mediator.domain.quest;

import com.alta.behaviorprocess.data.quest.QuestModel;

/**
 * Describes the provider of model related to interactions.
 */
public interface QuestDataProvider {

    /**
     * Gets the quest model.
     *
     * @param questName         - the name of quest to be retrieved.
     * @param currentStepNumber - the number of current step to be retrieved.
     * @return the {@link QuestModel} instance.
     */
    QuestModel getQuestModel(String questName, int currentStepNumber);

    /**
     * Gets the count of steps that available in quest.
     *
     * @param questName - the name of quest to search steps.
     * @return the count of steps.
     */
    int getCountOfStepsInQuest(String questName);

}
