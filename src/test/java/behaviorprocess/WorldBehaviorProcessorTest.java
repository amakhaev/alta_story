package behaviorprocess;

import com.alta.behaviorprocess.WorldBehaviorProcessor;
import com.alta.behaviorprocess.service.Behavior;
import com.alta.behaviorprocess.service.interaction.InteractionScenarioData;
import com.alta.behaviorprocess.service.quest.QuestScenarioData;
import com.alta.behaviorprocess.shared.scenario.Scenario;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WorldBehaviorProcessorTest {

    private Behavior<InteractionScenarioData> interactionBehavior;
    private Behavior<QuestScenarioData> mainQuestBehavior;
    private WorldBehaviorProcessor worldBehaviorProcessor;

    @Before
    public void setUp() {
        this.interactionBehavior = mock(Behavior.class);
        this.mainQuestBehavior = mock(Behavior.class);
        this.worldBehaviorProcessor = new WorldBehaviorProcessor(this.interactionBehavior, this.mainQuestBehavior);
    }

    @Test
    public void worldBehaviorProcessorTest_runProcessing_scenarioStartExecution() {
        Scenario scenario = mock(Scenario.class);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);

        this.worldBehaviorProcessor.runProcessing("testUuid", new Point());

        verify(scenario, times(1)).execute();
    }

    @Test
    public void worldBehaviorProcessorTest_runProcessingWithoutCoordinates_scenarioStartExecution() {
        Scenario scenario = mock(Scenario.class);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);

        this.worldBehaviorProcessor.runProcessing("testUuid", null);

        verify(scenario, times(1)).execute();
    }

    @Test
    public void worldBehaviorProcessorTest_runProcessingWhenAnotherProcessRunning_newScenarioIsNotRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(false);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);
        this.worldBehaviorProcessor.runProcessing("testUuid", null);
        verify(scenario, times(1)).execute();

        Scenario newScenario = mock(Scenario.class);
        when(this.interactionBehavior.getScenario(any())).thenReturn(newScenario);
        this.worldBehaviorProcessor.runProcessing("testUuid2", null);
        verify(newScenario, times(0)).execute();
    }

    @Test
    public void worldBehaviorProcessorTest_checkProcessRunning_processIsNotRunning() {
        Assert.assertFalse(this.worldBehaviorProcessor.isProcessRunning());
    }

    @Test
    public void worldBehaviorProcessorTest_processRunningWhenScenarioRunning_processIsRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(false);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);
        this.worldBehaviorProcessor.runProcessing("testUuid", null);

        Assert.assertTrue(this.worldBehaviorProcessor.isProcessRunning());
    }

    @Test
    public void worldBehaviorProcessorTest_processStoppedRunningWhenScenarioCompleted_processIsNotRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(true);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);
        this.worldBehaviorProcessor.runProcessing("testUuid", null);

        Assert.assertFalse(this.worldBehaviorProcessor.isProcessRunning());
    }

    @Test
    public void worldBehaviorProcessorTest_runNextStep_processIsNotRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(false);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);
        this.worldBehaviorProcessor.runProcessing("testUuid", null);

        this.worldBehaviorProcessor.runNextStep();
        verify(scenario, times(1)).runNextEffect();
    }

    @Test
    public void worldBehaviorProcessorTest_processRunningForQuest_processIsRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(false);
        when(this.mainQuestBehavior.getScenario(any())).thenReturn(scenario);
        this.worldBehaviorProcessor.runProcessing("testUuid", null);

        Assert.assertTrue(this.worldBehaviorProcessor.isProcessRunning());
        verify(this.mainQuestBehavior, times(1)).getScenario(any());
        verify(this.interactionBehavior, times(0)).getScenario(any());
    }
}
