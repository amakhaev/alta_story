package mediator;

import mediator.command.preservation.MakeSnapshotCommandTest;
import mediator.command.preservation.RestoreSnapshotCommandTest;
import mediator.command.preservation.UpdateActingCharacterCommandTest;
import mediator.dataSource.QuestRepositoryTest;
import mediator.domain.effect.EffectDataProviderTest;
import mediator.domain.interaction.InteractionDataProviderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EffectDataProviderTest.class,
        InteractionDataProviderTest.class,
        QuestRepositoryTest.class,
        MakeSnapshotCommandTest.class,
        RestoreSnapshotCommandTest.class,
        UpdateActingCharacterCommandTest.class
})
public class MediatorTestSuite {
}
