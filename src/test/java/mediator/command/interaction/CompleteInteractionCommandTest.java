package mediator.command.interaction;

import com.alta.dao.domain.preservation.interaction.InteractionPreservationService;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.interaction.CompleteInteractionCommand;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.mediator.command.preservation.UpdateInteractionPreservationCommand;
import com.alta.mediator.domain.interaction.InteractionPostProcessingService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CompleteInteractionCommandTest {

    private PreservationCommandFactory preservationCommandFactory;
    private CommandExecutor commandExecutor;
    private InteractionPostProcessingService interactionPostProcessingService;
    private InteractionPreservationService interactionPreservationService;
    private CompleteInteractionCommand command;

    @Before
    public void setUp() {
        this.preservationCommandFactory = mock(PreservationCommandFactory.class);
        this.commandExecutor = mock(CommandExecutor.class);
        this.interactionPostProcessingService = mock(InteractionPostProcessingService.class);
        this.interactionPreservationService = mock(InteractionPreservationService.class);

        this.command = new CompleteInteractionCommand(
                this.preservationCommandFactory,
                this.commandExecutor,
                this.interactionPostProcessingService,
                this.interactionPreservationService,
                1L,
                "interactionUuid",
                "relatedMapName"
        );
    }

    @Test
    public void completeInteractionCommand_executeCommand_executedSuccessful() {
        UpdateInteractionPreservationCommand updateInteractionPreservationCommand =
                new UpdateInteractionPreservationCommand(this.interactionPreservationService, null);

        when(this.preservationCommandFactory.createUpdateInteractionPreservationCommand(any())).thenReturn(
                updateInteractionPreservationCommand
        );

        this.command.execute();

        verify(this.preservationCommandFactory, times(1))
                .createUpdateInteractionPreservationCommand(any());
        verify(this.commandExecutor, times(1)).executeCommand(updateInteractionPreservationCommand);
        verify(this.interactionPostProcessingService, times(1)).
                executeInteractionPostProcessing("interactionUuid", "relatedMapName");
    }

}
