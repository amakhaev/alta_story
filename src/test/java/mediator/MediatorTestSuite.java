package mediator;

import mediator.command.interaction.CompleteInteractionCommandTest;
import mediator.command.preservation.UpdateGlobalPreservationCommandTest;
import mediator.command.quest.CompleteQuestStepCommandTest;
import mediator.dataSource.QuestRepositoryTest;
import mediator.domain.effect.BackgroundEffectServiceTest;
import mediator.domain.effect.EffectDataProviderTest;
import mediator.domain.interaction.InteractionDataProviderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BackgroundEffectServiceTest.class,
        EffectDataProviderTest.class,

        InteractionDataProviderTest.class,

        UpdateGlobalPreservationCommandTest.class,
        CompleteInteractionCommandTest.class,
        CompleteQuestStepCommandTest.class,

        QuestRepositoryTest.class
})
public class MediatorTestSuite {
}
