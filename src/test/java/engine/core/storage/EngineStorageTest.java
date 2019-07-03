package engine.core.storage;

import com.alta.engine.core.storage.EngineStorage;
import com.alta.engine.data.FrameStageEngineDataModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EngineStorageTest {

    private EngineStorage engineStorage;

    @Before
    public void setUp() {
        this.engineStorage = new EngineStorage();
    }

    @Test
    public void engineStorageTest_putFrameStageData_dataSaved() {
        FrameStageEngineDataModel data = FrameStageEngineDataModel.builder().mapName("testMapName").build();

        this.engineStorage.put(data);
        Assert.assertNotNull(this.engineStorage.getFrameStageData());
        Assert.assertEquals(data.getMapName(), this.engineStorage.getFrameStageData().getMapName());
    }

    @Test
    public void engineStorageTest_putNullFrameStageData_dataIsNull() {
        this.engineStorage.put(null);
        Assert.assertNull(this.engineStorage.getFrameStageData());
    }

    @Test
    public void engineStorageTest_refreshFrameStageData_dataRefreshed() {
        FrameStageEngineDataModel data1 = FrameStageEngineDataModel.builder().mapName("testMapName1").build();
        FrameStageEngineDataModel data2 = FrameStageEngineDataModel.builder().mapName("testMapName2").build();

        this.engineStorage.put(data1);
        this.engineStorage.put(data2);
        Assert.assertNotNull(this.engineStorage.getFrameStageData());
        Assert.assertEquals(data2.getMapName(), this.engineStorage.getFrameStageData().getMapName());
    }

    @Test
    public void engineStorageTest_refreshFrameStageDataToNull_dataIsNull() {
        FrameStageEngineDataModel data = FrameStageEngineDataModel.builder().mapName("testMapName1").build();

        this.engineStorage.put(data);
        Assert.assertNotNull(this.engineStorage.getFrameStageData());

        this.engineStorage.put(null);
        Assert.assertNull(this.engineStorage.getFrameStageData());
    }

}
