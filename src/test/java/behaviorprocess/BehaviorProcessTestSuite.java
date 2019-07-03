package behaviorprocess;

import behaviorprocess.core.DataStorageTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        WorldBehaviorProcessorTest.class,
        DataStorageTest.class
})
public class BehaviorProcessTestSuite {
}
