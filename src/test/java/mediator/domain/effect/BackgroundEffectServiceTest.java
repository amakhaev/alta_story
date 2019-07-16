package mediator.domain.effect;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.preservation.GlobalPreservationModel;
import com.alta.dao.domain.preservation.global.GlobalPreservationService;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.mediator.domain.effect.BackgroundEffectService;
import com.alta.mediator.domain.effect.BackgroundEffectServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BackgroundEffectServiceTest {

    private BackgroundEffectService backgroundEffectService;
    private CommandExecutor commandExecutor;
    private PreservationCommandFactory preservationCommandFactory;

    private GlobalPreservationModel globalPreservationModel;

    @Before
    public void setUp() {
        this.globalPreservationModel = new GlobalPreservationModel();
        this.globalPreservationModel.setChapterIndicator(1);
        this.globalPreservationModel.setTemporary(true);

        this.commandExecutor = mock(CommandExecutor.class);
        this.preservationCommandFactory = mock(PreservationCommandFactory.class);

        GlobalPreservationService globalPreservationService = mock(GlobalPreservationService.class);
        when(globalPreservationService.getTemporaryGlobalPreservation(anyLong())).thenReturn(this.globalPreservationModel);

        this.backgroundEffectService = new BackgroundEffectServiceImpl(
                this.commandExecutor,
                this.preservationCommandFactory,
                globalPreservationService,
                1L
        );
    }

    @Test
    public void backgroundEffectServiceTest_executeOneEffect_effectExecuted() {
        EffectDataModel effectDataModel = new EffectDataModel(EffectDataModel.EffectType.INCREMENT_CHAPTER_INDICATOR);

        this.backgroundEffectService.executeBackgroundEffects(Collections.singletonList(effectDataModel));

        verify(this.preservationCommandFactory, times(1)).createUpdatePreservationCommand(any());
        verify(this.commandExecutor, times(1)).executeCommand(any());
    }

    @Test
    public void backgroundEffectServiceTest_executeThreeEffect_effectsExecuted() {
        EffectDataModel effectDataModel1 = new EffectDataModel(EffectDataModel.EffectType.INCREMENT_CHAPTER_INDICATOR);
        EffectDataModel effectDataModel2 = new EffectDataModel(EffectDataModel.EffectType.INCREMENT_CHAPTER_INDICATOR);
        EffectDataModel effectDataModel3 = new EffectDataModel(EffectDataModel.EffectType.INCREMENT_CHAPTER_INDICATOR);

        this.backgroundEffectService.executeBackgroundEffects(Arrays.asList(effectDataModel1, effectDataModel2, effectDataModel3));

        verify(this.preservationCommandFactory, times(3)).createUpdatePreservationCommand(any());
        verify(this.commandExecutor, times(3)).executeCommand(any());
    }

    @Test
    public void backgroundEffectServiceTest_emptyEffectList_noActionsPerformed() {
        this.backgroundEffectService.executeBackgroundEffects(Collections.emptyList());

        verify(this.preservationCommandFactory, times(0)).createUpdatePreservationCommand(any());
        verify(this.commandExecutor, times(0)).executeCommand(any());
    }
}
