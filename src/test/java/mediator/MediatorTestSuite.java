package mediator;

import mediator.command.interaction.CompleteInteractionCommandTest;
import mediator.domain.EffectDataProviderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EffectDataProviderTest.class,
        CompleteInteractionCommandTest.class
})
public class MediatorTestSuite {
}
