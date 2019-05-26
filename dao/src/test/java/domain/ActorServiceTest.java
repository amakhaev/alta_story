package domain;

import com.alta.dao.data.actor.ActorDirectionModel;
import com.alta.dao.data.actor.ActorFaceSetDescriptorModel;
import com.alta.dao.data.actor.ActorModel;
import com.alta.dao.data.actor.ActorTileSetDescriptorModel;
import com.alta.dao.domain.actor.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class ActorServiceTest {

    @Mock
    private ActorTileSetDescriptorModel actorTileSetDescriptorModel;

    @Mock
    private ActorFaceSetDescriptorModel actorFaceSetDescriptorModel;

    @Mock
    private Map<String, ActorEntity> actors = new HashMap<>();

    private ActorService actorService;
    private ActorEntity actorEntity1;
    private ActorEntity actorEntity2;

    @Before
    public void setUp() {
        this.actorService = new ActorServiceImpl(
                mock(ActorTileSetDescriptorDeserializer.class),
                mock(ActorFaceSetDescriptoDeserializer.class)
        );

        this.actorTileSetDescriptorModel = ActorTileSetDescriptorModel.builder()
                .tileWidth(32)
                .tileHeight(32)
                .directionDown(Collections.singletonList(new ActorDirectionModel(0, 0, false)))
                .directionLeft(Collections.singletonList(new ActorDirectionModel(0, 0, false)))
                .directionRight(Collections.singletonList(new ActorDirectionModel(0, 0, false)))
                .directionUp(Collections.singletonList(new ActorDirectionModel(0, 0, false)))
                .build();

        this.actorFaceSetDescriptorModel = ActorFaceSetDescriptorModel.builder()
                .tileWidth(32)
                .tileHeight(32)
                .emotions(Collections.emptyMap())
                .build();

        actorEntity1 = new ActorEntity();
        actorEntity1.setName("actor1");
        actorEntity1.setTileSetImageName("tileSetActor1.png");
        actorEntity1.setFaceSetImageName("faceSetActor1.png");
        actorEntity1.setDurationTime(100);
        actorEntity1.setZIndex(5);

        actorEntity2 = new ActorEntity();
        actorEntity2.setName("actor2");
        actorEntity1.setTileSetImageName("tileSetActor2.png");
        actorEntity1.setFaceSetImageName("faceSetActor2.png");
        actorEntity2.setDurationTime(200);
        actorEntity2.setZIndex(10);

        this.actors.put(actorEntity1.getName(), actorEntity1);
        this.actors.put(actorEntity2.getName(), actorEntity2);
        Whitebox.setInternalState(this.actorService, "actors", actors);
        Whitebox.setInternalState(this.actorService, "actorTileSetDescriptorModel", actorTileSetDescriptorModel);

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
        Assert.assertEquals(actorEntity2.getZIndex(), actorModel.getZIndex());
        Assert.assertNotNull(actorModel.getTileSetDescriptor());
    }

}
