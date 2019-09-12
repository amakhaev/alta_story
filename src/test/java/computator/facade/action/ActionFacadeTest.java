package computator.facade.action;

import com.alta.computator.calculator.actingCharacter.ActingCharacterMediator;
import com.alta.computator.calculator.focusPoint.FocusPointMediator;
import com.alta.computator.calculator.npc.NpcMediator;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.facade.action.ActionFacade;
import com.alta.computator.facade.action.ActionFacadeImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.awt.*;
import java.util.function.Function;

import static org.mockito.Mockito.*;

public class ActionFacadeTest {

    private ActionFacade actionFacade;
    private ActingCharacterMediator actingCharacterMediator;
    private FocusPointMediator focusPointMediator;
    private NpcMediator npcMediator;

    @Before
    public void setUp() {
        this.actingCharacterMediator = mock(ActingCharacterMediator.class);
        this.focusPointMediator = mock(FocusPointMediator.class);
        this.npcMediator = mock(NpcMediator.class);

        this.actionFacade = new ActionFacadeImpl(this.actingCharacterMediator, this.focusPointMediator, this.npcMediator);
    }

    @Test
    public void actionFacade_tryToRunActingCharacterMovement_processRan() {
        ArgumentCaptor<MovementDirection> movementDirectionArgumentCaptor = ArgumentCaptor.forClass(MovementDirection.class);

        this.actionFacade.tryToRunActingCharacterMovement(MovementDirection.DOWN);
        verify(this.focusPointMediator, times(1)).tryToRunMovement(movementDirectionArgumentCaptor.capture());
        Assert.assertEquals(MovementDirection.DOWN, movementDirectionArgumentCaptor.getValue());
    }

    @Test
    public void actionFacade_setPauseForAll_pauseWasSetForFocusPointAndNpc() {
        ArgumentCaptor<Boolean> focusPointPauseArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Boolean> npcPauseArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);

        this.actionFacade.setPauseForAll(true);

        verify(this.focusPointMediator, times(1)).setPause(focusPointPauseArgumentCaptor.capture());
        verify(this.npcMediator, times(1)).setPause(npcPauseArgumentCaptor.capture());

        Assert.assertTrue(focusPointPauseArgumentCaptor.getValue());
        Assert.assertTrue(npcPauseArgumentCaptor.getValue());
    }

    @Test
    public void actionFacade_setNpcPause_pauseWasSetForNpcOnly() {
        ArgumentCaptor<Boolean> npcPauseArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<String> npcUuidArgumentCaptor = ArgumentCaptor.forClass(String.class);

        this.actionFacade.setNpcPause(true, "testUuid");

        verify(this.focusPointMediator, times(0)).setPause(true);
        verify(this.npcMediator, times(1)).setPause(npcPauseArgumentCaptor.capture(), npcUuidArgumentCaptor.capture());

        Assert.assertTrue(npcPauseArgumentCaptor.getValue());
        Assert.assertEquals("testUuid", npcUuidArgumentCaptor.getValue());
    }

    @Test
    public void actionFacade_tryToRunNpcMovement_pcMovementCalled() {
        ArgumentCaptor<String> uuidArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> xArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> speedArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<MovementDirection> directionArgumentCaptor = ArgumentCaptor.forClass(MovementDirection.class);
        ArgumentCaptor<Function<String, Void>> callbackArgumentCaptor = ArgumentCaptor.forClass(Function.class);

        this.actionFacade.tryToRunNpcMovement("testUuid", new Point(5, 10), 2, MovementDirection.UP, null);

        verify(this.npcMediator, times(1)).tryToRunNpcMovement(
                uuidArgumentCaptor.capture(),
                xArgumentCaptor.capture(),
                yArgumentCaptor.capture(),
                speedArgumentCaptor.capture(),
                directionArgumentCaptor.capture(),
                callbackArgumentCaptor.capture()
        );

        Assert.assertEquals("testUuid", uuidArgumentCaptor.getValue());
        Assert.assertEquals(new Integer(5), xArgumentCaptor.getValue());
        Assert.assertEquals(new Integer(10), yArgumentCaptor.getValue());
        Assert.assertEquals(new Integer(2), speedArgumentCaptor.getValue());
        Assert.assertEquals(MovementDirection.UP, directionArgumentCaptor.getValue());
    }
}
