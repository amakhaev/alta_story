import behaviorprocess.BehaviorProcessTestSuite;
import computator.ComputatorTestSuite;
import dao.DaoTestSuite;
import engine.EngineTestSuite;
import mediator.MediatorTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MediatorTestSuite.class,
        ComputatorTestSuite.class,
        DaoTestSuite.class,
        BehaviorProcessTestSuite.class,
        EngineTestSuite.class
})
public class AltaTestSuite {
}
