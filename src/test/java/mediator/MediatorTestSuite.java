package mediator;

import mediator.command.interaction.CompleteInteractionCommandTest;
import mediator.domain.InteractionDataProviderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        InteractionDataProviderTest.class,
        CompleteInteractionCommandTest.class
})
public class MediatorTestSuite {
}
