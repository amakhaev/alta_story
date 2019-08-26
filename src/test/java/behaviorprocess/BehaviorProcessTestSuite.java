package behaviorprocess;

import behaviorprocess.controller.GlobalEventControllerTest;
import behaviorprocess.controller.LocalMapControllerTest;
import behaviorprocess.controller.ScenarioControllerTest;
import behaviorprocess.sync.DataStorageTest;
import behaviorprocess.sync.SynchronizationManagerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ScenarioControllerTest.class,
        DataStorageTest.class,
        SynchronizationManagerTest.class,
        GlobalEventControllerTest.class,
        LocalMapControllerTest.class
})
public class BehaviorProcessTestSuite {
}
