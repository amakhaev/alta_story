package mediator.command.preservation;

import com.alta.dao.data.preservation.GlobalPreservationModel;
import com.alta.dao.domain.preservation.global.GlobalPreservationService;
import com.alta.mediator.command.preservation.UpdateGlobalPreservationCommand;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class UpdateGlobalPreservationCommandTest {

    private UpdateGlobalPreservationCommand updateGlobalPreservationCommand;
    private GlobalPreservationService globalPreservationService;

    @Before
    public void setUp() {
        this.globalPreservationService = mock(GlobalPreservationService.class);

        this.updateGlobalPreservationCommand = new UpdateGlobalPreservationCommand(
                this.globalPreservationService,
                new GlobalPreservationModel()
        );
    }

    @Test
    public void updateGlobalPreservationCommand_updateGlobalPreservation_upsertTemporaryCallad() {
        this.updateGlobalPreservationCommand.execute();
        verify(this.globalPreservationService, times(1)).upsertTemporaryQuestPreservation(any());
    }

}
