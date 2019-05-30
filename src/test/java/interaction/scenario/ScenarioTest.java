package interaction.scenario;

import com.alta.interaction.data.HideFacilityEffectModel;
import com.alta.interaction.data.ShowFacilityEffectModel;
import com.alta.interaction.scenario.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class ScenarioTest {

    private Scenario scenario;
    private Runnable completeCallback;
    private Runnable failCallback;

    @Before
    public void setUp() {
        EffectListener effectListener = mock(EffectListener.class);
        EffectFactory effectFactory = mock(EffectFactory.class);
        when(effectFactory.createHideFacilityInteraction(effectListener)).thenReturn(new HideFacilityEffect(effectListener));
        when(effectFactory.createShowFacilityInteraction(effectListener)).thenReturn(new ShowFacilityEffect(effectListener));
        when(effectFactory.createDialogueInteraction("test", effectListener)).thenReturn(new DialogueEffect("test", effectListener));

        this.completeCallback = mock(Runnable.class);
        this.failCallback = mock(Runnable.class);
        this.scenario = new Scenario(effectFactory, effectListener, this.completeCallback, this.failCallback);
    }

    @Test
    public void scenarioTest_hideFacility_effectCompleted() {
        HideFacilityEffectModel hideFacilityEffectModel = new HideFacilityEffectModel("test");
        this.scenario.performScenario("test", Collections.singletonList(hideFacilityEffectModel));

        verify(this.completeCallback, times(1)).run();
    }

    @Test
    public void scenarioTest_showFacility_effectCompleted() {
        ShowFacilityEffectModel showFacilityEffectModel = new ShowFacilityEffectModel("test");
        this.scenario.performScenario("test", Collections.singletonList(showFacilityEffectModel));

        verify(this.completeCallback, times(1)).run();
    }

    @Test
    public void scenarioTest_multipleFacilityEffects_effectsCompleted() {
        ShowFacilityEffectModel showFacilityEffectModel1 = new ShowFacilityEffectModel("show1");
        ShowFacilityEffectModel showFacilityEffectModel2 = new ShowFacilityEffectModel("show2");
        HideFacilityEffectModel hideFacilityEffectModel1 = new HideFacilityEffectModel("hide1");
        HideFacilityEffectModel hideFacilityEffectModel2 = new HideFacilityEffectModel("hide2");
        HideFacilityEffectModel hideFacilityEffectModel3 = new HideFacilityEffectModel("hide3");
        this.scenario.performScenario(
                "test",
                Arrays.asList(
                        showFacilityEffectModel1,
                        hideFacilityEffectModel2,
                        hideFacilityEffectModel1,
                        showFacilityEffectModel2,
                        hideFacilityEffectModel3
                ));

        verify(this.completeCallback, times(1)).run();
    }
}
