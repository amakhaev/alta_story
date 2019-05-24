package domain;

import com.alta.dao.data.actor.ActorDirectionModel;
import com.alta.dao.data.actor.ActorModel;
import com.alta.dao.domain.actor.ActorEntity;
import com.alta.dao.domain.actor.ActorService;
import com.alta.dao.domain.actor.ActorServiceImpl;
import com.alta.dao.domain.actor.TileSetDescriptorEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ActorServiceTest {

    @Mock
    private TileSetDescriptorEntity tileSetDescriptorEntity = new TileSetDescriptorEntity();

    @Mock
    private Map<String, ActorEntity> actors = new HashMap<>();

    private ActorService actorService;
    private ActorEntity actorEntity1;
    private ActorEntity actorEntity2;

    @Before
    public void setUp() {
        this.actorService = new ActorServiceImpl();

        this.tileSetDescriptorEntity.setTileWidth(32);
        this.tileSetDescriptorEntity.setTileHeight(32);
        this.tileSetDescriptorEntity.setDirectionDown(Collections.singletonList(new ActorDirectionModel()));
        this.tileSetDescriptorEntity.setDirectionLeft(Collections.singletonList(new ActorDirectionModel()));
        this.tileSetDescriptorEntity.setDirectionRight(Collections.singletonList(new ActorDirectionModel()));
        this.tileSetDescriptorEntity.setDirectionUp(Collections.singletonList(new ActorDirectionModel()));

        actorEntity1 = new ActorEntity();
        actorEntity1.setName("actor1");
        actorEntity1.setImageName("actor1.png");
        actorEntity1.setDurationTime(100);
        actorEntity1.setZIndex(5);

        actorEntity2 = new ActorEntity();
        actorEntity2.setName("actor2");
        actorEntity2.setImageName("actor2.png");
        actorEntity2.setDurationTime(200);
        actorEntity2.setZIndex(10);

        this.actors.put(actorEntity1.getName(), actorEntity1);
        this.actors.put(actorEntity2.getName(), actorEntity2);
        Whitebox.setInternalState(this.actorService, "actors", actors);
        Whitebox.setInternalState(this.actorService, "tileSetDescriptorEntity", tileSetDescriptorEntity);

    }

    @Test
    public void actorService_getNotExistsActor_returnNull() {
        ActorModel actorModel = this.actorService.getActorModel("notExists");
        Assert.assertNull(actorModel);
    }

    @Test
    public void actorService_getExistsActor_returnActor() {
        ActorModel actorModel = this.actorService.getActorModel("actor2");

        Assert.assertEquals(actorEntity2.getDurationTime(), actorModel.getDurationTime());
        Assert.assertEquals(actorEntity2.getZIndex(), actorModel.getZIndex());
        Assert.assertNotNull(actorModel.getDescriptor());
    }

}
