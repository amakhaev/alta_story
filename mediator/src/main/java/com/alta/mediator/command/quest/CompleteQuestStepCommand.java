package com.alta.mediator.command.quest;

import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.domain.preservation.quest.QuestPreservationService;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import javax.inject.Named;

/**
 * Provides the command that completes the step in quest.
 */
public class CompleteQuestStepCommand implements Command {

    private final PreservationCommandFactory preservationCommandFactory;
    private final CommandExecutor commandExecutor;
    private final QuestPreservationService questPreservationService;
    private final String questName;
    private final Long currentPreservationId;
    private final int stepCountInQuest;
    private final int stepNumber;

    /**
     * Initialize new instance of {@link CompleteQuestStepCommand}.
     * @param preservationCommandFactory    - the {@link PreservationCommandFactory} instance.
     * @param commandExecutor               - the {@link CommandExecutor} instance.
     * @param questPreservationService      - the {@link QuestPreservationService} instance.
     * @param currentPreservationId         - the current preservation id.
     * @param questName                     - the name of quest.
     * @param stepCountInQuest              - the count of steps in quest.
     * @param stepNumber                    - the number of current step.
     */
    @AssistedInject
    public CompleteQuestStepCommand(PreservationCommandFactory preservationCommandFactory,
                                    CommandExecutor commandExecutor,
                                    QuestPreservationService questPreservationService,
                                    @Named("currentPreservationId") Long currentPreservationId,
                                    @Assisted("questName") String questName,
                                    @Assisted("stepCountInQuest") int stepCountInQuest,
                                    @Assisted("stepNumber") int stepNumber) {
        this.preservationCommandFactory = preservationCommandFactory;
        this.commandExecutor = commandExecutor;
        this.questPreservationService = questPreservationService;
        this.questName = questName;
        this.currentPreservationId = currentPreservationId;
        this.stepCountInQuest = stepCountInQuest;
        this.stepNumber = stepNumber;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        QuestPreservationModel questToUpdate = this.questPreservationService.getTemporaryQuestPreservation(
                this.currentPreservationId, this.questName
        );

        if (questToUpdate == null) {
            questToUpdate = QuestPreservationModel.builder()
                    .name(this.questName)
                    .preservationId(this.currentPreservationId)
                    .currentStepNumber(this.stepCountInQuest > this.stepNumber ? this.stepNumber + 1 : this.stepNumber)
                    .isCompleted(this.stepCountInQuest >= this.stepNumber)
                    .build();
        } else {
            questToUpdate.setCurrentStepNumber(this.stepCountInQuest < this.stepNumber ? this.stepNumber + 1 : this.stepNumber);
            questToUpdate.setCompleted(this.stepCountInQuest >= this.stepNumber);
        }

        Command command = this.preservationCommandFactory.createUpdateQuestPreservationCommand(questToUpdate);
        this.commandExecutor.executeCommand(command);
    }
}
