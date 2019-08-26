package behaviorprocess.controller;

import com.alta.behaviorprocess.controller.globalEvent.GlobalEventController;
import com.alta.behaviorprocess.controller.globalEvent.GlobalEventControllerImpl;
import com.alta.behaviorprocess.data.globalEvent.GlobalEventRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.awt.*;

import static org.mockito.Mockito.*;

public class GlobalEventControllerTest {

    private GlobalEventController globalEventController;
    private GlobalEventRepository globalEventRepository;

    @Before
    public void setUp() {
        this.globalEventRepository = mock(GlobalEventRepository.class);
        this.globalEventController = new GlobalEventControllerImpl(this.globalEventRepository);
    }

    @Test
    public void globalEventController_saveGameState_repositoryMethodCalled() {
        ArgumentCaptor<String> mapNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> skinNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Point> positionArgumentCaptor = ArgumentCaptor.forClass(Point.class);

        this.globalEventController.saveGameState("testMap", "testSkin", new Point(4, 5));

        verify(this.globalEventRepository, times(1)).saveState(
                mapNameArgumentCaptor.capture(),
                skinNameArgumentCaptor.capture(),
                positionArgumentCaptor.capture()
        );

        Assert.assertEquals("testMap", mapNameArgumentCaptor.getValue());
        Assert.assertEquals("testSkin", skinNameArgumentCaptor.getValue());
        Assert.assertEquals(new Point(4, 5), positionArgumentCaptor.getValue());
    }

    @Test
    public void globalEventController_saveGameStateWithNullParameters_repositoryMethodCalled() {
        ArgumentCaptor<String> mapNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> skinNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Point> positionArgumentCaptor = ArgumentCaptor.forClass(Point.class);

        this.globalEventController.saveGameState(null, null, null);

        verify(this.globalEventRepository, times(1)).saveState(
                mapNameArgumentCaptor.capture(),
                skinNameArgumentCaptor.capture(),
                positionArgumentCaptor.capture()
        );

        Assert.assertNull(mapNameArgumentCaptor.getValue());
        Assert.assertNull(skinNameArgumentCaptor.getValue());
        Assert.assertNull(positionArgumentCaptor.getValue());
    }
}
