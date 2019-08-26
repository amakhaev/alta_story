package behaviorprocess.controller;

import com.alta.behaviorprocess.controller.scenario.ScenarioControllerImpl;
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

public class ScenarioControllerTest {

    private Behavior<InteractionScenarioData> interactionBehavior;
    private Behavior<QuestScenarioData> mainQuestBehavior;
    private ScenarioControllerImpl scenarioControllerImpl;

    @Before
    public void setUp() {
        this.interactionBehavior = mock(Behavior.class);
        this.mainQuestBehavior = mock(Behavior.class);
        this.scenarioControllerImpl = new ScenarioControllerImpl(this.interactionBehavior, this.mainQuestBehavior);
    }

    @Test
    public void worldBehaviorProcessorTest_runProcessing_scenarioStartExecution() {
        Scenario scenario = mock(Scenario.class);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);

        this.scenarioControllerImpl.runScenario("testUuid", new Point());

        verify(scenario, times(1)).execute();
    }

    @Test
    public void worldBehaviorProcessorTest_runProcessingWithoutCoordinates_scenarioStartExecution() {
        Scenario scenario = mock(Scenario.class);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);

        this.scenarioControllerImpl.runScenario("testUuid", null);

        verify(scenario, times(1)).execute();
    }

    @Test
    public void worldBehaviorProcessorTest_runProcessingWhenAnotherProcessRunning_newScenarioIsNotRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(false);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);
        this.scenarioControllerImpl.runScenario("testUuid", null);
        verify(scenario, times(1)).execute();

        Scenario newScenario = mock(Scenario.class);
        when(this.interactionBehavior.getScenario(any())).thenReturn(newScenario);
        this.scenarioControllerImpl.runScenario("testUuid2", null);
        verify(newScenario, times(0)).execute();
    }

    @Test
    public void worldBehaviorProcessorTest_checkProcessRunning_processIsNotRunning() {
        Assert.assertFalse(this.scenarioControllerImpl.isScenarioRunning());
    }

    @Test
    public void worldBehaviorProcessorTest_processRunningWhenScenarioRunning_processIsRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(false);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);
        this.scenarioControllerImpl.runScenario("testUuid", null);

        Assert.assertTrue(this.scenarioControllerImpl.isScenarioRunning());
    }

    @Test
    public void worldBehaviorProcessorTest_processStoppedRunningWhenScenarioCompleted_processIsNotRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(true);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);
        this.scenarioControllerImpl.runScenario("testUuid", null);

        Assert.assertFalse(this.scenarioControllerImpl.isScenarioRunning());
    }

    @Test
    public void worldBehaviorProcessorTest_runNextStep_processIsNotRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(false);
        when(this.interactionBehavior.getScenario(any())).thenReturn(scenario);
        this.scenarioControllerImpl.runScenario("testUuid", null);

        this.scenarioControllerImpl.runNextScenarioStep();
        verify(scenario, times(1)).runNextEffect();
    }

    @Test
    public void worldBehaviorProcessorTest_processRunningForQuest_processIsRunning() {
        Scenario scenario = mock(Scenario.class);
        when(scenario.isCompleted()).thenReturn(false);
        when(this.mainQuestBehavior.getScenario(any())).thenReturn(scenario);
        this.scenarioControllerImpl.runScenario("testUuid", null);

        Assert.assertTrue(this.scenarioControllerImpl.isScenarioRunning());
        verify(this.mainQuestBehavior, times(1)).getScenario(any());
        verify(this.interactionBehavior, times(0)).getScenario(any());
    }
}
