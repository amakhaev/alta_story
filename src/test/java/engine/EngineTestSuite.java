package engine;

import engine.core.storage.EngineStorageTest;
import engine.facade.EffectListenerTest;
import engine.facade.FrameStageFacadeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EngineStorageTest.class,
        EffectListenerTest.class,
        FrameStageFacadeTest.class
})
public class EngineTestSuite {
}
