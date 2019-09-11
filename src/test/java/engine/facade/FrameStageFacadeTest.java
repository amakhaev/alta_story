package engine.facade;

import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.engine.core.storage.EngineStorage;
import com.alta.engine.data.FrameStageEngineDataModel;
import com.alta.engine.data.frameStage.JumpingEngineModel;
import com.alta.engine.facade.FrameStageFacade;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.presenter.MessageBoxPresenter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class FrameStageFacadeTest {

    private FrameStageFacade frameStageFacade;
    private FrameStagePresenter frameStagePresenter;
    private MessageBoxPresenter messageBoxPresenter;
    private EngineStorage engineStorage;

    @Before
    public void setUp() {
        this.frameStagePresenter = mock(FrameStagePresenter.class);
        this.messageBoxPresenter = mock(MessageBoxPresenter.class);
        this.engineStorage = mock(EngineStorage.class);

        this.frameStageFacade = new FrameStageFacade(
                this.frameStagePresenter, this.messageBoxPresenter, this.engineStorage
        );
    }

    @Test
    public void frameStageFacade_startScene_sceneStartCalledFromPresenter() {
        this.frameStageFacade.startScene();
        verify(this.frameStagePresenter, times(1)).startScene();
    }

    @Test
    public void frameStageFacade_performMovement_performMovementCalledFromPresenter() {
        ArgumentCaptor<MovementDirection> movementDirectionArgumentCaptor = ArgumentCaptor.forClass(MovementDirection.class);

        this.frameStageFacade.movementPerform(MovementDirection.DOWN);
        verify(this.frameStagePresenter, times(1)).movementPerform(movementDirectionArgumentCaptor.capture());

        Assert.assertEquals(MovementDirection.DOWN, movementDirectionArgumentCaptor.getValue());
    }

    @Test
    public void frameStageFacade_getActingCharacterMapCoordinates_returnCoordinatesOfActingCharacter() {
        when(this.frameStagePresenter.getActingCharacterMapCoordinate()).thenReturn(new Point(3, 5));
        Assert.assertEquals(new Point(3, 5), this.frameStageFacade.getActingCharacterMapCoordinate());
    }

    @Test
    public void frameStageFacade_replaceFacility_calledRemoveFacilityThenAddFacility() {
        ArgumentCaptor<String> hideFacilityArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> showFacilityArgumentCaptor = ArgumentCaptor.forClass(String.class);

        this.frameStageFacade.replaceFacility("hide_uuid", "show_uuid");

        verify(this.frameStagePresenter, times(1)).removeFacility(hideFacilityArgumentCaptor.capture());
        verify(this.frameStagePresenter, times(1)).addFacility(showFacilityArgumentCaptor.capture());

        Assert.assertEquals("hide_uuid", hideFacilityArgumentCaptor.getValue());
        Assert.assertEquals("show_uuid", showFacilityArgumentCaptor.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void frameStageFacade_replaceFacilityNullHideFacilityUuid_throwException() {
        this.frameStageFacade.replaceFacility(null, "show_uuid");
    }

    @Test(expected = NullPointerException.class)
    public void frameStageFacade_replaceFacilityNullShowFacilityUuid_throwException() {
        this.frameStageFacade.replaceFacility("hide_uuid", null);
    }

    @Test
    public void frameStageFacade_findJumPoint_returnFoundPoint() {
        List<JumpingEngineModel> points = Arrays.asList(
                JumpingEngineModel.builder().from(new Point(1, 1)).build(),
                JumpingEngineModel.builder().from(new Point(2, 2)).build(),
                JumpingEngineModel.builder().from(new Point(3, 3)).build()
        );

        FrameStageEngineDataModel engineDataModel = FrameStageEngineDataModel.builder().jumpingPoints(points).build();
        when(this.engineStorage.getFrameStageData()).thenReturn(engineDataModel);

        Assert.assertEquals(new Point(3, 3), this.frameStageFacade.findJumpingPoint(new Point(3, 3)).getFrom());
        Assert.assertNull(this.frameStageFacade.findJumpingPoint(new Point(3, 2)));
        Assert.assertNull(this.frameStageFacade.findJumpingPoint(new Point(2, 3)));
    }

    @Test
    public void frameStageFacade_renderFrameStage_viewRenderedAndMessageBoxShown() {
        ArgumentCaptor<String> titleArgumentCaptor = ArgumentCaptor.forClass(String.class);

        FrameStageEngineDataModel engineDataModel = FrameStageEngineDataModel.builder().mapDisplayName("my map").build();
        when(this.engineStorage.getFrameStageData()).thenReturn(engineDataModel);

        this.frameStageFacade.tryToRenderFrameStageView();

        verify(this.frameStagePresenter, times(1)).tryToRenderFrameStageView();
        verify(this.messageBoxPresenter, times(1)).forceHideMessageBox();
        verify(this.messageBoxPresenter, times(1)).showTitle(titleArgumentCaptor.capture());

        Assert.assertEquals("my map", titleArgumentCaptor.getValue());
    }
}
