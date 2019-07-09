package mediator.dataSource;

import com.alta.behaviorprocess.data.quest.QuestModel;
import com.alta.behaviorprocess.data.quest.QuestRepository;
import com.alta.behaviorprocess.data.quest.QuestStepModel;
import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.data.quest.AvailableQuest;
import com.alta.dao.domain.preservation.quest.QuestPreservationService;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.quest.CompleteQuestStepCommand;
import com.alta.mediator.command.quest.QuestCommandFactory;
import com.alta.mediator.dataSource.QuestRepositoryImpl;
import com.alta.mediator.domain.quest.QuestDataProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class QuestRepositoryTest {

    private QuestRepository questRepository;
    private QuestPreservationService questPreservationService;
    private QuestDataProvider questDataProvider;
    private QuestCommandFactory questCommandFactory;
    private CommandExecutor commandExecutor;

    private QuestPreservationModel mainQuestPreservationModel;
    private QuestModel mainQuestModel;

    @Before
    public void setUp() {
        this.questPreservationService = mock(QuestPreservationService.class);
        this.questDataProvider = mock(QuestDataProvider.class);
        this.commandExecutor = mock(CommandExecutor.class);
        this.questCommandFactory = mock(QuestCommandFactory.class);

        this.questRepository = new QuestRepositoryImpl(
                1L,
                this.questPreservationService,
                this.questDataProvider,
                this.questCommandFactory,
                this.commandExecutor
        );

        this.mainQuestPreservationModel = QuestPreservationModel.builder()
                .preservationId(1L)
                .currentStepNumber(1)
                .name(AvailableQuest.MAIN_QUEST)
                .build();

        this.mainQuestModel = QuestModel.builder()
                .name(AvailableQuest.MAIN_QUEST)
                .currentStep(QuestStepModel.builder().stepNumber(1).build())
                .build();
    }

    @Test
    public void questRepository_getMainQuestFromSaved_retrievedSuccessfully() {
        when(this.questPreservationService.getQuestPreservation(1L, AvailableQuest.MAIN_QUEST))
                .thenReturn(this.mainQuestPreservationModel);

        when(this.questDataProvider.getQuestModel(AvailableQuest.MAIN_QUEST, 1)).thenReturn(this.mainQuestModel);

        QuestModel questModel = this.questRepository.getMainQuest();
        Assert.assertEquals(AvailableQuest.MAIN_QUEST, questModel.getName());
        Assert.assertEquals(1, questModel.getCurrentStep().getStepNumber());
        verify(this.questPreservationService, times(1))
                .getTemporaryQuestPreservation(1L, AvailableQuest.MAIN_QUEST);
        verify(this.questPreservationService, times(1))
                .getQuestPreservation(1L, AvailableQuest.MAIN_QUEST);
    }

    @Test
    public void questRepository_getMainQuestFromTemporary_retrievedSuccessfully() {
        when(this.questPreservationService.getTemporaryQuestPreservation(1L, AvailableQuest.MAIN_QUEST))
                .thenReturn(this.mainQuestPreservationModel);

        when(this.questDataProvider.getQuestModel(AvailableQuest.MAIN_QUEST, 1)).thenReturn(this.mainQuestModel);

        QuestModel questModel = this.questRepository.getMainQuest();
        Assert.assertEquals(AvailableQuest.MAIN_QUEST, questModel.getName());
        Assert.assertEquals(1, questModel.getCurrentStep().getStepNumber());
        verify(this.questPreservationService, times(1))
                .getTemporaryQuestPreservation(1L, AvailableQuest.MAIN_QUEST);
        verify(this.questPreservationService, times(0)).getQuestPreservation(any(), any());
    }

    @Test
    public void questRepository_completeQuest_calledCompleteCommand() {
        CompleteQuestStepCommand command = mock(CompleteQuestStepCommand.class);

        when(this.questDataProvider.getCountOfStepsInQuest(AvailableQuest.MAIN_QUEST)).thenReturn(5);
        when(this.questCommandFactory.createCompleteQuestStepCommand(
                AvailableQuest.MAIN_QUEST, 5, 1)).thenReturn(command);

        this.questRepository.completeQuestStep("main", 1);

        verify(this.questDataProvider, times(1)).getCountOfStepsInQuest(AvailableQuest.MAIN_QUEST);
        verify(this.questCommandFactory, times(1)).createCompleteQuestStepCommand(
                AvailableQuest.MAIN_QUEST, 5, 1
        );
        verify(this.commandExecutor, times(1)).executeCommand(command);
    }

    @Test
    public void questRepository_completeQuestInvalidName_logErrorAndReturn() {
        this.questRepository.completeQuestStep(null, 1);

        verify(this.questDataProvider, times(0)).getCountOfStepsInQuest(any());
        verify(this.questCommandFactory, times(0)).createCompleteQuestStepCommand(anyString(), anyInt(), anyInt());
        verify(this.commandExecutor, times(0)).executeCommand(any());
    }

    @Test
    public void questRepository_completeQuestStepNumber_logErrorAndReturn() {
        this.questRepository.completeQuestStep("main", -1);

        verify(this.questDataProvider, times(0)).getCountOfStepsInQuest(any());
        verify(this.questCommandFactory, times(0)).createCompleteQuestStepCommand(anyString(), anyInt(), anyInt());
        verify(this.commandExecutor, times(0)).executeCommand(any());
    }
}
