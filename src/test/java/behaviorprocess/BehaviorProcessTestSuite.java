package behaviorprocess;

import behaviorprocess.sync.DataStorageTest;
import behaviorprocess.sync.SynchronizationManagerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        WorldBehaviorProcessorTest.class,
        DataStorageTest.class,
        SynchronizationManagerTest.class
})
public class BehaviorProcessTestSuite {
}
