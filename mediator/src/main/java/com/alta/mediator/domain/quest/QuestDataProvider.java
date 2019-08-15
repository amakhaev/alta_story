package com.alta.mediator.domain.quest;

import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.dao.data.common.effect.EffectDataModel;

import java.util.List;

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
     * Gets the list of post effects from quest.
     *
     * @param questName         - the name of quest to be retrieved.
     * @param currentStepNumber - the number of current step to be retrieved.
     * @return the {@link List} of post effects.
     */
    List<EffectDataModel> getBackgroundPostEffects(String questName, int currentStepNumber);

    /**
     * Gets the count of steps that available in quest.
     *
     * @param questName - the name of quest to search steps.
     * @return the count of steps.
     */
    int getCountOfStepsInQuest(String questName);

}
