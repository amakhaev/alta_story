package computator;

import computator.calculator.actingCharacter.ActingCharacterMediatorTest;
import computator.facade.action.ActionFacadeTest;
import computator.facade.dataReader.DataReaderFacadeTest;
import computator.utils.MovementRouteComputatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MovementRouteComputatorTest.class,
        ActingCharacterMediatorTest.class,
        ActionFacadeTest.class,
        DataReaderFacadeTest.class
})
public class ComputatorTestSuite {
}
