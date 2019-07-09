package com.alta.behaviorprocess.data.quest;

/**
 * Provides the repository of to make CRUD with quests.
 */
public interface QuestRepository {

    /**
     * Gets the main quest.
     */
    QuestModel getMainQuest();

    /**
     * Completes the step from quest.
     *
     * @param name          - the name of quest.
     * @param stepNumber    - the step that was completed.
     */
    void completeQuestStep(String name, int stepNumber);

}
