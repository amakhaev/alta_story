package mediator.command.quest;

import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.domain.preservation.quest.QuestPreservationService;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.mediator.command.quest.CompleteQuestStepCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class CompleteQuestStepCommandTest {

    private CompleteQuestStepCommand completeQuestStepCommand;
    private PreservationCommandFactory preservationCommandFactory;
    private CommandExecutor commandExecutor;
    private QuestPreservationService questPreservationService;

    @Before
    public void setUp() {
        this.preservationCommandFactory = mock(PreservationCommandFactory.class);
        this.commandExecutor = mock(CommandExecutor.class);
        this.questPreservationService = mock(QuestPreservationService.class);

        this.completeQuestStepCommand = new CompleteQuestStepCommand(
                this.preservationCommandFactory,
                this.commandExecutor,
                this.questPreservationService,
                1L,
                "testQuestName",
                5,
                1
        );
    }

    @Test
    public void completeQuestStepCommand_temporaryQuestNotExists_createdNewTemporarySave() {
        when(this.questPreservationService.getTemporaryQuestPreservation(1L, "testQuestName")).thenReturn(null);
        ArgumentCaptor<QuestPreservationModel> questPreservationArgumentCaptor = ArgumentCaptor.forClass(QuestPreservationModel.class);

        this.completeQuestStepCommand.execute();

        verify(this.preservationCommandFactory, times(1))
                .createUpdateQuestPreservationCommand(questPreservationArgumentCaptor.capture());
        verify(this.commandExecutor, times(1)).executeCommand(any());

        Assert.assertEquals("testQuestName", questPreservationArgumentCaptor.getValue().getName());
        Assert.assertEquals(2, questPreservationArgumentCaptor.getValue().getCurrentStepNumber());
        Assert.assertEquals(1L, questPreservationArgumentCaptor.getValue().getPreservationId().longValue());
    }

    @Test
    public void completeQuestStepCommand_temporaryQuestExists_updateTemporarySave() {
        QuestPreservationModel existsQuestPreservation = QuestPreservationModel.builder()
                .name("testQuestName")
                .preservationId(1L)
                .currentStepNumber(0)
                .isCompleted(false)
                .build();

        when(this.questPreservationService.getTemporaryQuestPreservation(1L, "testQuestName")).thenReturn(existsQuestPreservation);
        ArgumentCaptor<QuestPreservationModel> questPreservationArgumentCaptor = ArgumentCaptor.forClass(QuestPreservationModel.class);

        this.completeQuestStepCommand.execute();

        verify(this.preservationCommandFactory, times(1))
                .createUpdateQuestPreservationCommand(questPreservationArgumentCaptor.capture());
        verify(this.commandExecutor, times(1)).executeCommand(any());

        Assert.assertEquals("testQuestName", questPreservationArgumentCaptor.getValue().getName());
        Assert.assertEquals(1, questPreservationArgumentCaptor.getValue().getCurrentStepNumber());
        Assert.assertEquals(1L, questPreservationArgumentCaptor.getValue().getPreservationId().longValue());
        Assert.assertTrue(questPreservationArgumentCaptor.getValue().isCompleted());
    }
}
