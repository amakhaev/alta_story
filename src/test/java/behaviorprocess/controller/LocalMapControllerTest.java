package behaviorprocess.controller;

import com.alta.behaviorprocess.controller.localMap.LocalMapController;
import com.alta.behaviorprocess.controller.localMap.LocalMapControllerImpl;
import com.alta.behaviorprocess.data.localMap.LocalMapRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.awt.*;

import static org.mockito.Mockito.*;

public class LocalMapControllerTest {

    private LocalMapController localMapController;
    private LocalMapRepository localMapRepository;

    @Before
    public void setUp() {
        this.localMapRepository = mock(LocalMapRepository.class);
        this.localMapController = new LocalMapControllerImpl(this.localMapRepository);
    }

    @Test
    public void localMapController_jumpToMap_repositoryMethodCalled() {
        ArgumentCaptor<String> mapNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Point> mapCoordinateArgumentCaptor = ArgumentCaptor.forClass(Point.class);

        this.localMapController.jumpToMap("testMap", new Point(4, 3));

        verify(this.localMapRepository, times(1)).makeJumping(
                mapNameArgumentCaptor.capture(), mapCoordinateArgumentCaptor.capture()
        );

        Assert.assertEquals("testMap", mapNameArgumentCaptor.getValue());
        Assert.assertEquals( new Point(4, 3), mapCoordinateArgumentCaptor.getValue());
    }

    @Test
    public void localMapController_jumpToMapWithNullParameters_repositoryMethodCalled() {
        ArgumentCaptor<String> mapNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Point> mapCoordinateArgumentCaptor = ArgumentCaptor.forClass(Point.class);

        this.localMapController.jumpToMap(null, null);

        verify(this.localMapRepository, times(1)).makeJumping(
                mapNameArgumentCaptor.capture(), mapCoordinateArgumentCaptor.capture()
        );

        Assert.assertNull(mapNameArgumentCaptor.getValue());
        Assert.assertNull(mapCoordinateArgumentCaptor.getValue());
    }

}
