package mediator.command.preservation;

import com.alta.dao.domain.snapshot.PreservationSnapshotService;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.preservation.MakeSnapshotCommand;
import com.alta.mediator.command.preservation.RestoreSnapshotCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class RestoreSnapshotCommandTest {

    private PreservationSnapshotService preservationSnapshotService;

    @Before
    public void setUp() {
        this.preservationSnapshotService = mock(PreservationSnapshotService.class);
    }

    @Test
    public void restoreSnapshotCommand_restoreFromSnapshot_executedSuccessfully() {
        ArgumentCaptor<Integer> clearPreservationIdArgumentCapture = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> restorePreservationIdArgumentCapture = ArgumentCaptor.forClass(Integer.class);

        Command command = new RestoreSnapshotCommand(1, this.preservationSnapshotService);
        command.execute();

        verify(this.preservationSnapshotService, times(1)).clearTemporaryStorage(clearPreservationIdArgumentCapture.capture());
        verify(this.preservationSnapshotService, times(1)).restoreFromSnapshot(restorePreservationIdArgumentCapture.capture());

        Assert.assertEquals(1, clearPreservationIdArgumentCapture.getValue().intValue());
        Assert.assertEquals(1, restorePreservationIdArgumentCapture.getValue().intValue());
    }

    @Test(expected = NullPointerException.class)
    public void makeSnapshotCommand_makeSnapshotWithNullService_expectException() {
        Command command = new RestoreSnapshotCommand(1, null);
        command.execute();
    }

}
