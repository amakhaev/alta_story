package com.alta.mediator.command.quest;

import com.google.inject.assistedinject.Assisted;

/**
 * Provides the factory to create commands related to quests.
 */
public interface QuestCommandFactory {

    /**
     * Creates the {@link CompleteQuestStepCommand}.
     *
     * @param questName         - the name of quest.
     * @param stepCountInQuest  - the count of steps in quest.
     * @param stepNumber        - the number of current step.
     * @return
     */
    CompleteQuestStepCommand createCompleteQuestStepCommand(@Assisted("questName") String questName,
                                                            @Assisted("stepCountInQuest") int stepCountInQuest,
                                                            @Assisted("stepNumber") int stepNumber);

}
