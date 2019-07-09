package mediator.dataSource;

import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.interaction.InteractionPreservationService;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.interaction.CompleteInteractionCommand;
import com.alta.mediator.command.interaction.InteractionCommandFactory;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.mediator.dataSource.InteractionRepositoryImpl;
import com.alta.mediator.domain.interaction.InteractionDataProvider;
import com.alta.mediator.domain.interaction.InteractionPostProcessingService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class InteractionRepositoryImplTest {

    private final static String INTERACTION_UUID = "interactionUuid";
    private final static String RELATED_MAP_NAME = "mapName";

    private CommandExecutor commandExecutor;
    private InteractionCommandFactory interactionCommandFactory;

    private InteractionRepositoryImpl interactionRepository;

    @Before
    public void setUp() {
        this.commandExecutor = mock(CommandExecutor.class);
        this.interactionCommandFactory = mock(InteractionCommandFactory.class);

        this.interactionRepository = new InteractionRepositoryImpl(
                mock(PreservationService.class),
                mock(InteractionPreservationService.class),
                mock(InteractionDataProvider.class),
                this.interactionCommandFactory,
                this.commandExecutor,
                1L
        );
    }

    @Test
    public void interactionRepository_completeInteractionCorrectArguments_commandExecutedSuccessfully() {
        CompleteInteractionCommand command = new CompleteInteractionCommand(
                mock(PreservationCommandFactory.class),
                this.commandExecutor,
                mock(InteractionPostProcessingService.class),
                mock(InteractionPreservationService.class),
                1L,
                INTERACTION_UUID,
                RELATED_MAP_NAME
        );

        when(this.interactionCommandFactory.createCompleteInteractionCommand(INTERACTION_UUID, RELATED_MAP_NAME))
                .thenReturn(command);

        this.interactionRepository.completeInteraction(INTERACTION_UUID, RELATED_MAP_NAME);

        verify(this.interactionCommandFactory, times(1)).createCompleteInteractionCommand(
                INTERACTION_UUID, RELATED_MAP_NAME
        );
        verify(this.commandExecutor, times(1)).executeCommand(command);
    }

    @Test(expected = NullPointerException.class)
    public void interactionRepository_nullInteractionUuid_commandExecutedFailed() {
        this.interactionRepository.completeInteraction(null, RELATED_MAP_NAME);
    }

    @Test(expected = NullPointerException.class)
    public void interactionRepository_nullRelatedMapName_commandExecutedFailed() {
        this.interactionRepository.completeInteraction(INTERACTION_UUID, null);
    }

    @Test(expected = NullPointerException.class)
    public void interactionRepository_nullArguments_commandExecutedFailed() {
        this.interactionRepository.completeInteraction(INTERACTION_UUID, null);
    }

}
