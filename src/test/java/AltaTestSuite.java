import computator.ComputatorTestSuite;
import dao.DaoTestSuite;
import interaction.InteractionTestSuite;
import mediator.MediatorTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MediatorTestSuite.class,
        ComputatorTestSuite.class,
        DaoTestSuite.class,
        InteractionTestSuite.class
})
public class AltaTestSuite {
}
