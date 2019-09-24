package com.alta.mediator.dataSource;

import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.behaviorprocess.data.quest.QuestRepository;
import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.data.quest.AvailableQuest;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.quest.QuestCommandFactory;
import com.alta.mediator.domain.quest.QuestDataProvider;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Provides the repository of to make CRUD with quests.
 */
@Slf4j
public class QuestRepositoryImpl implements QuestRepository {

    private final Long currentPreservationId;
    private final PreservationService preservationService;
    private final QuestDataProvider questDataProvider;
    private final QuestCommandFactory questCommandFactory;
    private final CommandExecutor commandExecutor;

    /**
     * Initialize ew instance of {@link QuestRepositoryImpl}.
     * @param currentPreservationId     - the Id of current preservation.
     * @param preservationService       - the {@link PreservationService} instance.
     * @param questDataProvider         - the {@link QuestDataProvider} instance.
     * @param questCommandFactory       - the {@link QuestCommandFactory} instance.
     * @param commandExecutor           - the {@link CommandExecutor} instance.
     */
    @Inject
    public QuestRepositoryImpl(@Named("currentPreservationId") Long currentPreservationId,
                               PreservationService preservationService,
                               QuestDataProvider questDataProvider,
                               QuestCommandFactory questCommandFactory,
                               CommandExecutor commandExecutor) {
        this.currentPreservationId = currentPreservationId;
        this.preservationService = preservationService;
        this.questDataProvider = questDataProvider;
        this.questCommandFactory = questCommandFactory;
        this.commandExecutor = commandExecutor;
    }

    /**
     * Gets the main quest.
     */
    @Override
    public QuestModel getMainQuest() {
        QuestPreservationModel questPreservationModel = this.preservationService.getQuest(
                this.currentPreservationId.intValue(), AvailableQuest.MAIN_QUEST
        );

        if (questPreservationModel == null) {
            throw new RuntimeException("The main quest not found in database");
        }

        return this.questDataProvider.getQuestModel(
                questPreservationModel.getQuestName(), questPreservationModel.getCurrentStepNumber()
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

        List<EffectDataModel> backgroundPostEffects = this.questDataProvider.getBackgroundPostEffects(name, stepNumber);
        int stepCountInQuest = this.questDataProvider.getCountOfStepsInQuest(name);
        Command command = this.questCommandFactory.createCompleteQuestStepCommand(
                name, backgroundPostEffects, stepCountInQuest, stepNumber
        );
        this.commandExecutor.executeCommand(command);
    }
}
