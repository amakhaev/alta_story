package computator.facade.dataReader;

import com.alta.computator.calculator.actingCharacter.ActingCharacterMediator;
import com.alta.computator.calculator.facility.FacilityMediator;
import com.alta.computator.calculator.map.MapMediator;
import com.alta.computator.calculator.npc.NpcMediator;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.core.storage.StorageReader;
import com.alta.computator.facade.dataReader.DataReaderFacade;
import com.alta.computator.facade.dataReader.DataReaderFacadeImpl;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.model.participant.actor.RouteNpcParticipant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataReaderFacadeTest {

    private DataReaderFacade dataReaderFacade;
    private NpcMediator npcMediator;
    private ActingCharacterMediator actingCharacterMediator;
    private MapMediator mapMediator;
    private StorageReader storageReader;
    private FacilityMediator facilityMediator;

    @Before
    public void setUp() {
        this.actingCharacterMediator = mock(ActingCharacterMediator.class);
        this.npcMediator = mock(NpcMediator.class);
        this.mapMediator = mock(MapMediator.class);
        this.storageReader = mock(StorageReader.class);
        this.facilityMediator = mock(FacilityMediator.class);

        this.dataReaderFacade = new DataReaderFacadeImpl(
                this.mapMediator,
                this.storageReader,
                this.npcMediator,
                this.actingCharacterMediator,
                this.facilityMediator
        );
    }

    @Test
    public void dataReaderFacade_getMapGlobalCoordinates_returnsCoordinatesOfMap() {
        when(this.mapMediator.getMapGlobalCoordinates()).thenReturn(new Point(3, 1));
        Assert.assertEquals(new Point(3, 1), this.dataReaderFacade.getMapGlobalCoordinates());
    }

    @Test
    public void dataReaderFacade_findActingCharacterByUuid_returnsFoundActingCharacter() {
        ActingCharacterParticipant participant = new ActingCharacterParticipant("testUuid", new Point(), 4);
        when(this.storageReader.getActingCharacter()).thenReturn(participant);

        ActorParticipant actorParticipant = this.dataReaderFacade.findActorByUuid("testUuid");

        Assert.assertNotNull(actorParticipant);
        Assert.assertEquals(actorParticipant.getUuid(), "testUuid");
    }

    @Test
    public void dataReaderFacade_findNpcByUuid_returnsFoundNpc() {
        ActorParticipant actorParticipant = new RouteNpcParticipant(
                "testUuid", new Point(), 4, 1000, MovementDirection.DOWN, false, null
        );

        when(this.npcMediator.getParticipant("testUuid")).thenReturn(actorParticipant);

        ActorParticipant participant = this.dataReaderFacade.findActorByUuid("testUuid");

        Assert.assertNotNull(participant);
        Assert.assertEquals(participant.getUuid(), "testUuid");
    }

    @Test
    public void dataReaderFacade_findActorByUuid_returnsNull() {
        ActorParticipant participant = this.dataReaderFacade.findActorByUuid("testUuid");
        Assert.assertNull(participant);
    }

}
