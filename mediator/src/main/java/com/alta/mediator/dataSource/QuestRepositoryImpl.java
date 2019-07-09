package com.alta.mediator.dataSource;

import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.behaviorprocess.data.quest.QuestRepository;
import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.data.quest.AvailableQuest;
import com.alta.dao.domain.preservation.quest.QuestPreservationService;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.quest.QuestCommandFactory;
import com.alta.mediator.domain.quest.QuestDataProvider;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Provides the repository of to make CRUD with quests.
 */
@Slf4j
public class QuestRepositoryImpl implements QuestRepository {

    private final Long currentPreservationId;
    private final QuestPreservationService questPreservationService;
    private final QuestDataProvider questDataProvider;
    private final QuestCommandFactory questCommandFactory;
    private final CommandExecutor commandExecutor;

    /**
     * Initialize ew instance of {@link QuestRepositoryImpl}.
     * @param currentPreservationId     - the Id of current preservation.
     * @param questPreservationService  - the {@link QuestPreservationService} instance.
     * @param questDataProvider         - the {@link QuestDataProvider} instance.
     * @param questCommandFactory       - the {@link QuestCommandFactory} instance.
     * @param commandExecutor           - the {@link CommandExecutor} instance.
     */
    @Inject
    public QuestRepositoryImpl(@Named("currentPreservationId") Long currentPreservationId,
                               QuestPreservationService questPreservationService,
                               QuestDataProvider questDataProvider,
                               QuestCommandFactory questCommandFactory,
                               CommandExecutor commandExecutor) {
        this.currentPreservationId = currentPreservationId;
        this.questPreservationService = questPreservationService;
        this.questDataProvider = questDataProvider;
        this.questCommandFactory = questCommandFactory;
        this.commandExecutor = commandExecutor;
    }

    /**
     * Gets the main quest.
     */
    @Override
    public QuestModel getMainQuest() {
        QuestPreservationModel questPreservationModel = this.findPreservationForMainQuest();

        if (questPreservationModel == null) {
            throw new RuntimeException("The main quest not found in database");
        }

        return this.questDataProvider.getQuestModel(
                questPreservationModel.getName(), questPreservationModel.getCurrentStepNumber()
        );
    }

    /**
     * Completes the step from quest.
     *
     * @param name       - the name of quest.
     * @param stepNumber - the step that was completed.
     */
    @Override
    public void completeQuestStep(String name, int stepNumber) {
        if (Strings.isNullOrEmpty(name) || stepNumber < 0) {
            log.error("Invalid arguments for completing step of quest. name: {}, step number: {}", name, stepNumber);
            return;
        }

        int stepCountInQuest = this.questDataProvider.getCountOfStepsInQuest(name);
        Command command = this.questCommandFactory.createCompleteQuestStepCommand(name, stepCountInQuest, stepNumber);
        this.commandExecutor.executeCommand(command);
    }

    private QuestPreservationModel findPreservationForMainQuest() {
        // Try to get temporary saved quest since it newly model.
        QuestPreservationModel questPreservationModel = this.questPreservationService.getTemporaryQuestPreservation(
                this.currentPreservationId, AvailableQuest.MAIN_QUEST
        );

        if (questPreservationModel == null) {
            // Try to get saved quest.
            questPreservationModel = this.questPreservationService.getQuestPreservation(
                    this.currentPreservationId, AvailableQuest.MAIN_QUEST
            );
        }

        return questPreservationModel;
    }
}
