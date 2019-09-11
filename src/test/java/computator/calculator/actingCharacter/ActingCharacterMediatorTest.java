package computator.calculator.actingCharacter;

import com.alta.computator.calculator.CalculatorCache;
import com.alta.computator.calculator.actingCharacter.ActingCharacterMediator;
import com.alta.computator.calculator.actingCharacter.ActingCharacterMediatorImpl;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.core.storage.StorageReader;
import com.alta.computator.model.participant.actor.ActingCharacterParticipant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActingCharacterMediatorTest {

    private ActingCharacterMediator actingCharacterMediator;
    private StorageReader storageReader;
    ActingCharacterParticipant participant;

    @Before
    public void setUp() {
        this.storageReader = mock(StorageReader.class);
        this.actingCharacterMediator = new ActingCharacterMediatorImpl(this.storageReader, mock(CalculatorCache.class));

        this.participant = new ActingCharacterParticipant("testUuid", new Point(1, 1), 5);
        this.participant.updateCurrentMapCoordinates(5, 5);
        when(this.storageReader.getActingCharacter()).thenReturn(this.participant);
    }

    @Test
    public void actingCharacterMediator_getMapCoordinatesOfTargetParticipantRight_returnCorrectResult() {
        this.participant.setCurrentDirection(MovementDirection.RIGHT);
        Point foundCoordinates = this.actingCharacterMediator.getMapCoordinatesOfTargetParticipant();
        Assert.assertEquals(new Point(6, 5), foundCoordinates);
    }

    @Test
    public void actingCharacterMediator_getMapCoordinatesOfTargetParticipantDown_returnCorrectResult() {
        this.participant.setCurrentDirection(MovementDirection.DOWN);
        Point foundCoordinates = this.actingCharacterMediator.getMapCoordinatesOfTargetParticipant();
        Assert.assertEquals(new Point(5, 6), foundCoordinates);
    }

    @Test
    public void actingCharacterMediator_getMapCoordinatesOfTargetParticipantLeft_returnCorrectResult() {
        this.participant.setCurrentDirection(MovementDirection.LEFT);
        Point foundCoordinates = this.actingCharacterMediator.getMapCoordinatesOfTargetParticipant();
        Assert.assertEquals(new Point(4, 5), foundCoordinates);
    }

    @Test
    public void actingCharacterMediator_getMapCoordinatesOfTargetParticipantUp_returnCorrectResult() {
        this.participant.setCurrentDirection(MovementDirection.UP);
        Point foundCoordinates = this.actingCharacterMediator.getMapCoordinatesOfTargetParticipant();
        Assert.assertEquals(new Point(5, 4), foundCoordinates);
    }

    @Test
    public void actingCharacterMediator_getMapCoordinatesOfTargetParticipantEmpty_returnCorrectResult() {
        this.participant.setCurrentDirection(null);
        Point foundCoordinates = this.actingCharacterMediator.getMapCoordinatesOfTargetParticipant();
        Assert.assertEquals(new Point(5, 6), foundCoordinates);
    }

}
