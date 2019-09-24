package mediator.command.preservation;

import com.alta.dao.data.preservation.udt.ActingCharacterUdt;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.preservation.UpdateActingCharacterCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class UpdateActingCharacterCommandTest {

    private PreservationService preservationService;

    @Before
    public void setUp() {
        this.preservationService = mock(PreservationService.class);
    }

    @Test
    public void updateActingCharacterCommand_update_executedSuccessfully() {
        ArgumentCaptor<Integer> preservationIdArgumentCapture = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<ActingCharacterUdt> characterIdArgumentCapture = ArgumentCaptor.forClass(ActingCharacterUdt.class);

        ActingCharacterUdt actingCharacter = ActingCharacterUdt.builder()
                .focusX(10)
                .focusY(20)
                .mapName("testMap")
                .skin("testSkin")
                .build();

        Command command = new UpdateActingCharacterCommand(1, actingCharacter, this.preservationService);
        command.execute();

        verify(this.preservationService, times(1)).updateActingCharacter(
                preservationIdArgumentCapture.capture(), characterIdArgumentCapture.capture()
        );

        Assert.assertEquals(1, preservationIdArgumentCapture.getValue().intValue());
        Assert.assertEquals(actingCharacter.getFocusX(), characterIdArgumentCapture.getValue().getFocusX());
        Assert.assertEquals(actingCharacter.getFocusY(), characterIdArgumentCapture.getValue().getFocusY());
        Assert.assertEquals(actingCharacter.getMapName(), characterIdArgumentCapture.getValue().getMapName());
        Assert.assertEquals(actingCharacter.getSkin(), characterIdArgumentCapture.getValue().getSkin());
    }

}
