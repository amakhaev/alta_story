package com.alta.mediator.command.quest;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.google.inject.assistedinject.Assisted;

import java.util.List;

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
                                                            @Assisted("postEffects") List<EffectDataModel> postEffects,
                                                            @Assisted("stepCountInQuest") int stepCountInQuest,
                                                            @Assisted("stepNumber") int stepNumber);

}
