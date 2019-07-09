package mediator;

import mediator.command.interaction.CompleteInteractionCommandTest;
import mediator.command.quest.CompleteQuestStepCommandTest;
import mediator.dataSource.QuestRepositoryTest;
import mediator.domain.EffectDataProviderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EffectDataProviderTest.class,
        CompleteInteractionCommandTest.class,
        CompleteQuestStepCommandTest.class,
        QuestRepositoryTest.class
})
public class MediatorTestSuite {
}
