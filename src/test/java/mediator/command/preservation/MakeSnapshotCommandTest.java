package mediator.command.preservation;

import com.alta.dao.domain.snapshot.PreservationSnapshotService;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.preservation.MakeSnapshotCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class MakeSnapshotCommandTest {

    private PreservationSnapshotService preservationSnapshotService;

    @Before
    public void setUp() {
        this.preservationSnapshotService = mock(PreservationSnapshotService.class);
    }

    @Test
    public void makeSnapshotCommand_makeSnapshot_executedSuccessfully() {
        ArgumentCaptor<Integer> preservationIdArgumentCapture = ArgumentCaptor.forClass(Integer.class);

        Command command = new MakeSnapshotCommand(1, this.preservationSnapshotService);
        command.execute();

        verify(this.preservationSnapshotService, times(1)).makeSnapshot(preservationIdArgumentCapture.capture());
        Assert.assertEquals(1, preservationIdArgumentCapture.getValue().intValue());
    }

    @Test(expected = NullPointerException.class)
    public void makeSnapshotCommand_makeSnapshotWithNullService_expectException() {
        Command command = new MakeSnapshotCommand(1, null);
        command.execute();
    }

}
