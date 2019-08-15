package com.alta.mediator.command.quest;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.domain.preservation.quest.QuestPreservationService;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.mediator.domain.effect.BackgroundEffectService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import javax.inject.Named;
import java.util.List;

/**
 * Provides the command that completes the step in quest.
 */
public class CompleteQuestStepCommand implements Command {

    private final PreservationCommandFactory preservationCommandFactory;
    private final CommandExecutor commandExecutor;
    private final QuestPreservationService questPreservationService;
    private final BackgroundEffectService backgroundEffectService;
    private final String questName;
    private final Long currentPreservationId;
    private final List<EffectDataModel> postEffects;
    private final int stepCountInQuest;
    private final int stepNumber;

    /**
     * Initialize new instance of {@link CompleteQuestStepCommand}.
     * @param preservationCommandFactory    - the {@link PreservationCommandFactory} instance.
     * @param commandExecutor               - the {@link CommandExecutor} instance.
     * @param questPreservationService      - the {@link QuestPreservationService} instance.
     * @param backgroundEffectService       - the {@link BackgroundEffectService} instance.
     * @param currentPreservationId         - the current preservation id.
     * @param questName                     - the name of quest.
     * @param postEffects                   - the effects to be executed.
     * @param stepCountInQuest              - the count of steps in quest.
     * @param stepNumber                    - the number of current step.
     */
    @AssistedInject
    public CompleteQuestStepCommand(PreservationCommandFactory preservationCommandFactory,
                                    CommandExecutor commandExecutor,
                                    QuestPreservationService questPreservationService,
                                    BackgroundEffectService backgroundEffectService,
                                    @Named("currentPreservationId") Long currentPreservationId,
                                    @Assisted("questName") String questName,
                                    @Assisted("postEffects") List<EffectDataModel> postEffects,
                                    @Assisted("stepCountInQuest") int stepCountInQuest,
                                    @Assisted("stepNumber") int stepNumber) {
        this.preservationCommandFactory = preservationCommandFactory;
        this.commandExecutor = commandExecutor;
        this.questPreservationService = questPreservationService;
        this.backgroundEffectService = backgroundEffectService;
        this.questName = questName;
        this.currentPreservationId = currentPreservationId;
        this.postEffects = postEffects;
        this.stepCountInQuest = stepCountInQuest;
        this.stepNumber = stepNumber;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.updateQuestPreservation();

        if (this.postEffects != null) {
            this.backgroundEffectService.executeBackgroundEffects(this.postEffects);
        }
    }

    private void updateQuestPreservation() {
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
            questToUpdate.setCompleted(this.stepCountInQuest <= this.stepNumber);
        }

        Command command = this.preservationCommandFactory.createUpdateQuestPreservationCommand(questToUpdate);
        this.commandExecutor.executeCommand(command);
    }
}
