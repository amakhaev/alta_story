package com.alta.dao.domain.quest;

import com.alta.dao.data.quest.QuestModel;

/**
 * Provides the service to make CRUD operations with quests.
 */
public interface QuestService {

    /**
     * Gets the quest by given path.
     *
     * @param path - the path to descriptor file.
     * @return found {@link QuestModel} instance.
     */
    QuestModel getQuest(String path);

    /**
     * Gets the quest by given path and specified list of steps.
     *
     * @param path                  - the path to descriptor file.
     * @param chapterIndicatorFrom  - the character indicator from.
     * @param chapterIndicatorTo    - the character indicator to.
     * @return found {@link QuestModel} instance.
     */
    QuestModel getQuestWithSpecifiedSteps(String path, int chapterIndicatorFrom, int chapterIndicatorTo);

    /**
     * Gets the quest path and specified list of steps.
     *
     * @param path              - the path to descriptor file.
     * @param currentStepNumber - the number of current step.
     * @return found {@link QuestModel} instance.
     */
    QuestModel getQuestWithSpecifiedSteps(String path, int currentStepNumber);


}
