package engine.facade;

import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectListener;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import com.alta.engine.facade.EffectListenerImpl;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class EffectListenerTest {

    private EffectListener effectListener;
    private FrameStagePresenter frameStagePresenter;
    private MessageBoxPresenter messageBoxPresenter;

    @Before
    public void setUp() {
        this.frameStagePresenter = mock(FrameStagePresenter.class);
        this.messageBoxPresenter = mock(MessageBoxPresenter.class);

        this.effectListener = new EffectListenerImpl(this.frameStagePresenter, this.messageBoxPresenter);
    }

    @Test
    public void effectListener_showSimpleMessage_messageDialogIsCalled() {
        when(this.messageBoxPresenter.isDialogueBoxOpen()).thenReturn(false);

        ArgumentCaptor<String> showDialogArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> startInteractionArgumentCaptor = ArgumentCaptor.forClass(String.class);

        this.effectListener.onShowMessage("my_uuid", "test message");

        verify(this.messageBoxPresenter, times(1)).showDialogueMessage(showDialogArgumentCaptor.capture());
        verify(this.frameStagePresenter, times(1)).startInteractionWithNpc(startInteractionArgumentCaptor.capture());

        Assert.assertEquals("test message", showDialogArgumentCaptor.getValue());
        Assert.assertEquals("my_uuid", startInteractionArgumentCaptor.getValue());
    }

    @Test
    public void effectListener_showSimpleMessageWhenDialogAlreadyOpened_messageDialogIsNotCalled() {
        when(this.messageBoxPresenter.isDialogueBoxOpen()).thenReturn(true);

        this.effectListener.onShowMessage("my_uuid", "test message");

        verify(this.messageBoxPresenter, times(0)).showDialogueMessage(anyString());
        verify(this.frameStagePresenter, times(0)).startInteractionWithNpc(anyString());
    }

    @Test
    public void effectListener_triggerNextState_showNextMessage() {
        when(this.messageBoxPresenter.isDialogueBoxOpen()).thenReturn(true);
        this.effectListener.onTriggerNextStateForMessage("my_uuid", null);
        verify(this.messageBoxPresenter, times(1)).tryToHideMessageBox();
    }

    @Test
    public void effectListener_hideFacility_facilityRemoved() {
        ArgumentCaptor<String> hideFacilityArgumentCaptor = ArgumentCaptor.forClass(String.class);

        this.effectListener.onHideFacility("my_uuid");
        verify(this.frameStagePresenter, times(1)).removeFacility(hideFacilityArgumentCaptor.capture());

        Assert.assertEquals("my_uuid", hideFacilityArgumentCaptor.getValue());
    }

    @Test
    public void effectListener_showFacility_facilityAdded() {
        ArgumentCaptor<String> showFacilityArgumentCaptor = ArgumentCaptor.forClass(String.class);

        this.effectListener.onShowFacility("my_uuid");
        verify(this.frameStagePresenter, times(1)).addFacility(showFacilityArgumentCaptor.capture());

        Assert.assertEquals("my_uuid", showFacilityArgumentCaptor.getValue());
    }

    @Test
    public void effectListener_routeMovement_movementPerformed() {
        ArgumentCaptor<String> uuidArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> xArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> speedArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<MovementDirection> finalDirectionArgumentCaptor = ArgumentCaptor.forClass(MovementDirection.class);

        this.effectListener.onRouteMovement("my_uuid", 5, 6, 10, "UP");

        verify(this.frameStagePresenter, times(1)).movementPerform(
                uuidArgumentCaptor.capture(),
                xArgumentCaptor.capture(),
                yArgumentCaptor.capture(),
                speedArgumentCaptor.capture(),
                finalDirectionArgumentCaptor.capture()
        );

        Assert.assertEquals("my_uuid", uuidArgumentCaptor.getValue());
        Assert.assertEquals(5, xArgumentCaptor.getValue().intValue());
        Assert.assertEquals(6, yArgumentCaptor.getValue().intValue());
        Assert.assertEquals(10, speedArgumentCaptor.getValue().intValue());
        Assert.assertEquals(MovementDirection.UP, finalDirectionArgumentCaptor.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void effectListener_routeMovementWrongFinalDirection_movementPerformFailed() {
        this.effectListener.onRouteMovement("my_uuid", 5, 6, 10, "WRONG_DIRECTION");
    }
}
