package behaviorprocess.core;

import com.alta.behaviorprocess.core.DataStorage;
import com.alta.behaviorprocess.data.interaction.InteractionModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataStorageTest {

    private DataStorage dataStorage;

    @Before
    public void setUp() {
        this.dataStorage = new DataStorage();
    }

    @Test
    public void dataStorageTest_saveMap_savedSuccessfully() {
        this.dataStorage.evictAndSaveCurrentMap("testMap");
        Assert.assertEquals("testMap", this.dataStorage.getCurrentMap());
    }

    @Test
    public void dataStorageTest_evictAndSaveMap_savedSuccessfully() {
        this.dataStorage.evictAndSaveCurrentMap("testMap");
        Assert.assertEquals("testMap", this.dataStorage.getCurrentMap());

        this.dataStorage.evictAndSaveCurrentMap("testMap2");
        Assert.assertEquals("testMap2", this.dataStorage.getCurrentMap());
    }

    @Test
    public void dataStorageTest_saveMapAsNull_savedSuccessfully() {
        this.dataStorage.evictAndSaveCurrentMap(null);
        Assert.assertNull(this.dataStorage.getCurrentMap());
    }

    @Test
    public void dataStorageTest_evictAndSaveMapAsNull_savedSuccessfully() {
        this.dataStorage.evictAndSaveCurrentMap("testMap");
        Assert.assertEquals("testMap", this.dataStorage.getCurrentMap());

        this.dataStorage.evictAndSaveCurrentMap(null);
        Assert.assertNull(this.dataStorage.getCurrentMap());
    }

    @Test
    public void dataStorageTest_saveInteractions_savedSuccessfully() {
        InteractionModel interaction1 = InteractionModel.builder().mapName("testName1").build();
        InteractionModel interaction2 = InteractionModel.builder().mapName("testName2").build();

        List<InteractionModel> interactions = new ArrayList<>();
        interactions.add(interaction1);
        interactions.add(interaction2);

        this.dataStorage.evictAndSaveInteractions(interactions);
        Assert.assertEquals(interactions.size(), this.dataStorage.getInteractions().size());
        Assert.assertEquals(interactions.get(0).getMapName(), this.dataStorage.getInteractions().get(0).getMapName());
        Assert.assertEquals(interactions.get(1).getMapName(), this.dataStorage.getInteractions().get(1).getMapName());
    }

    @Test
    public void dataStorageTest_evictAndSaveInteractions_savedSuccessfully() {
        InteractionModel interaction1 = InteractionModel.builder().mapName("testName1").build();
        InteractionModel interaction2 = InteractionModel.builder().mapName("testName2").build();

        this.dataStorage.evictAndSaveInteractions(Collections.singletonList(interaction1));
        Assert.assertEquals(1, this.dataStorage.getInteractions().size());
        Assert.assertEquals(interaction1.getMapName(), this.dataStorage.getInteractions().get(0).getMapName());

        this.dataStorage.evictAndSaveInteractions(Collections.singletonList(interaction2));
        Assert.assertEquals(interaction2.getMapName(), this.dataStorage.getInteractions().get(0).getMapName());
    }
}
