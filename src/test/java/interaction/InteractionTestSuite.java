package interaction;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import interaction.scenario.ScenarioTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ScenarioTest.class
})
public class InteractionTestSuite {
}
