package mediator.dataSource;

import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.behaviorprocess.data.quest.QuestRepository;
import com.alta.behaviorprocess.data.quest.QuestStepModel;
import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.data.quest.AvailableQuest;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.quest.CompleteQuestStepCommand;
import com.alta.mediator.command.quest.QuestCommandFactory;
import com.alta.mediator.dataSource.QuestRepositoryImpl;
import com.alta.mediator.domain.quest.QuestDataProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class QuestRepositoryTest {

    private QuestRepository questRepository;
    private QuestDataProvider questDataProvider;
    private QuestCommandFactory questCommandFactory;
    private CommandExecutor commandExecutor;
    private PreservationService preservationService;

    private QuestPreservationModel mainQuestPreservationModel;
    private QuestModel mainQuestModel;

    @Before
    public void setUp() {
        this.questDataProvider = mock(QuestDataProvider.class);
        this.commandExecutor = mock(CommandExecutor.class);
        this.questCommandFactory = mock(QuestCommandFactory.class);
        this.preservationService = mock(PreservationService.class);

        this.questRepository = new QuestRepositoryImpl(
                1L,
                this.preservationService,
                this.questDataProvider,
                this.questCommandFactory,
                this.commandExecutor
        );

        this.mainQuestPreservationModel = QuestPreservationModel.builder()
                .preservationId(1)
                .currentStepNumber(1)
                .questName(AvailableQuest.MAIN_QUEST)
                .build();

        this.mainQuestModel = QuestModel.builder()
                .name(AvailableQuest.MAIN_QUEST)
                .currentStep(QuestStepModel.builder().stepNumber(1).build())
                .build();
    }

    @Test
    public void questRepository_getMainQuestFromSaved_retrievedSuccessfully() {
        when(this.preservationService.getQuest(1, AvailableQuest.MAIN_QUEST)).thenReturn(this.mainQuestPreservationModel);
        when(this.questDataProvider.getQuestModel(AvailableQuest.MAIN_QUEST, 1)).thenReturn(this.mainQuestModel);

        QuestModel questModel = this.questRepository.getMainQuest();
        Assert.assertEquals(AvailableQuest.MAIN_QUEST, questModel.getName());
        Assert.assertEquals(1, questModel.getCurrentStep().getStepNumber());
    }

    @Test
    public void questRepository_completeQuest_calledCompleteCommand() {
        CompleteQuestStepCommand command = mock(CompleteQuestStepCommand.class);

        when(this.questDataProvider.getCountOfStepsInQuest(AvailableQuest.MAIN_QUEST)).thenReturn(5);
        when(this.questCommandFactory.createCompleteQuestStepCommand(
                AvailableQuest.MAIN_QUEST, Collections.emptyList(),5, 1)).thenReturn(command);

        this.questRepository.completeQuestStep("main", 1);

        verify(this.questDataProvider, times(1)).getCountOfStepsInQuest(AvailableQuest.MAIN_QUEST);
        verify(this.questCommandFactory, times(1)).createCompleteQuestStepCommand(
                AvailableQuest.MAIN_QUEST, Collections.emptyList(), 5, 1
        );
        verify(this.commandExecutor, times(1)).executeCommand(command);
    }

    @Test
    public void questRepository_completeQuestInvalidName_logErrorAndReturn() {
        this.questRepository.completeQuestStep(null, 1);

        verify(this.questDataProvider, times(0)).getCountOfStepsInQuest(any());
        verify(this.questCommandFactory, times(0)).createCompleteQuestStepCommand(
                anyString(), anyList(), anyInt(), anyInt()
        );
        verify(this.commandExecutor, times(0)).executeCommand(any());
    }

    @Test
    public void questRepository_completeQuestStepNumber_logErrorAndReturn() {
        this.questRepository.completeQuestStep("main", -1);

        verify(this.questDataProvider, times(0)).getCountOfStepsInQuest(any());
        verify(this.questCommandFactory, times(0)).createCompleteQuestStepCommand(
                anyString(), anyList(), anyInt(), anyInt()
        );
        verify(this.commandExecutor, times(0)).executeCommand(any());
    }
}
