package mediator.domain;

import com.alta.dao.data.interaction.InteractionDataModel;
import com.alta.dao.data.interaction.effect.DialogueEffectDataModel;
import com.alta.dao.data.interaction.effect.HideFacilityEffectDataModel;
import com.alta.dao.data.interaction.effect.InteractionEffectDataModel;
import com.alta.dao.data.interaction.effect.ShowFacilityEffectDataModel;
import com.alta.dao.domain.interaction.InteractionService;
import com.alta.engine.model.InteractionEngineDataModel;
import com.alta.mediator.domain.interaction.InteractionConditionService;
import com.alta.mediator.domain.interaction.InteractionDataProvider;
import com.alta.mediator.domain.interaction.InteractionDataProviderImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InteractionDataProviderTest {

    private InteractionService interactionService;
    private InteractionDataProvider interactionDataProvider;
    private InteractionDataModel dataModel1;
    private InteractionDataModel dataModel2;

    @Before
    public void setUp() {
        this.interactionService = mock(InteractionService.class);
        this.interactionDataProvider = new InteractionDataProviderImpl(this.interactionService, mock(InteractionConditionService.class));

        InteractionEffectDataModel dialogueEffect = DialogueEffectDataModel.builder()
                .text("my text")
                .speakerUuid("dialogEffectId")
                .speakerEmotion("CALM")
                .build();

        ShowFacilityEffectDataModel showFacilityEffect = new ShowFacilityEffectDataModel();
        showFacilityEffect.setFacilityUuid("showFacilityEffect");

        HideFacilityEffectDataModel hideFacilityEffect = new HideFacilityEffectDataModel();
        hideFacilityEffect.setFacilityUuid("hideFacilityEffect");

        this.dataModel1 = InteractionDataModel.builder()
                .uuid("dataModelId1")
                .targetUuid("targetId1")
                .chapterIndicatorFrom(1)
                .chapterIndicatorTo(10)
                .effects(Arrays.asList(dialogueEffect, showFacilityEffect, hideFacilityEffect))
                .build();

        this.dataModel2 = InteractionDataModel.builder()
                .uuid("dataModelId2")
                .targetUuid("targetId2")
                .chapterIndicatorFrom(5)
                .chapterIndicatorTo(15)
                .effects(Arrays.asList(dialogueEffect, showFacilityEffect, hideFacilityEffect))
                .build();
    }

    @Test
    public void interactionDataProvider_oneInteraction_returnCreatedEngineModel() {
        when(this.interactionService.getInteractions(anyString())).thenReturn(Collections.singletonList(this.dataModel1));

        InteractionEngineDataModel engineDataModel = this.interactionDataProvider.getInteractionByRelatedMapName(
                "relatedMap", Collections.emptyList(), 5
        );

        Assert.assertEquals(1, engineDataModel.getInteractions().size());
        Assert.assertEquals(this.dataModel1.getUuid(), engineDataModel.getInteractions().get(0).getUuid());
        Assert.assertEquals(this.dataModel1.getTargetUuid(), engineDataModel.getInteractions().get(0).getTargetUuid());
        Assert.assertNull(engineDataModel.getInteractions().get(0).getNext());
        Assert.assertEquals(0, engineDataModel.getInteractions().get(0).getFailedPreConditionInteractionEffects().size());
        Assert.assertEquals(0, engineDataModel.getInteractions().get(0).getShiftTiles().size());
        Assert.assertNull(engineDataModel.getInteractions().get(0).getPreCondition());
        Assert.assertFalse(engineDataModel.getInteractions().get(0).isCompleted());
        Assert.assertEquals(this.dataModel1.getEffects().size(), engineDataModel.getInteractions().get(0).getInteractionEffects().size());
    }

    @Test
    public void interactionDataProvider_oneInteractionWithInvalidChapter_returnEmptyList() {
        when(this.interactionService.getInteractions(anyString())).thenReturn(Collections.singletonList(this.dataModel1));

        InteractionEngineDataModel engineDataModel = this.interactionDataProvider.getInteractionByRelatedMapName(
                "relatedMap", Collections.emptyList(), 15
        );

        Assert.assertNotNull(engineDataModel);
        Assert.assertEquals(0, engineDataModel.getInteractions().size());
    }

    @Test
    public void interactionDataProvider_twoInteraction_returnOne() {
        when(this.interactionService.getInteractions(anyString())).thenReturn(Arrays.asList(this.dataModel1, this.dataModel2));

        InteractionEngineDataModel engineDataModel = this.interactionDataProvider.getInteractionByRelatedMapName(
                "relatedMap", Collections.emptyList(), 12
        );

        Assert.assertNotNull(engineDataModel);
        Assert.assertEquals(1, engineDataModel.getInteractions().size());
        Assert.assertEquals(this.dataModel2.getUuid(), engineDataModel.getInteractions().get(0).getUuid());
        Assert.assertEquals(this.dataModel2.getTargetUuid(), engineDataModel.getInteractions().get(0).getTargetUuid());
        Assert.assertNull(engineDataModel.getInteractions().get(0).getNext());
        Assert.assertEquals(0, engineDataModel.getInteractions().get(0).getFailedPreConditionInteractionEffects().size());
        Assert.assertEquals(0, engineDataModel.getInteractions().get(0).getShiftTiles().size());
        Assert.assertNull(engineDataModel.getInteractions().get(0).getPreCondition());
        Assert.assertFalse(engineDataModel.getInteractions().get(0).isCompleted());
        Assert.assertEquals(this.dataModel2.getEffects().size(), engineDataModel.getInteractions().get(0).getInteractionEffects().size());
    }

    @Test
    public void interactionDataProvider_unknownMap_returnEmptyList() {
        when(this.interactionService.getInteractions("test1")).thenReturn(Arrays.asList(this.dataModel1, this.dataModel2));

        InteractionEngineDataModel engineDataModel = this.interactionDataProvider.getInteractionByRelatedMapName(
                "relatedMap", Collections.emptyList(), 5
        );

        Assert.assertNotNull(engineDataModel);
        Assert.assertEquals(0, engineDataModel.getInteractions().size());
    }

    @Test
    public void interactionDataProvider_includeNextItem_returnOneItemWithNext() {
        InteractionDataModel interactionDataModel = InteractionDataModel.builder()
                .uuid("dataModelId3")
                .targetUuid("targetId3")
                .chapterIndicatorFrom(5)
                .chapterIndicatorTo(15)
                .effects(Collections.emptyList())
                .nextInteractionUuid(this.dataModel1.getUuid())
                .build();

        when(this.interactionService.getInteractions(anyString())).thenReturn(Arrays.asList(this.dataModel1, this.dataModel2, interactionDataModel));

        InteractionEngineDataModel engineDataModel = this.interactionDataProvider.getInteractionByRelatedMapName(
                "relatedMap", Collections.emptyList(), 10
        );

        Assert.assertNotNull(engineDataModel);
        Assert.assertEquals(3, engineDataModel.getInteractions().size());
        Assert.assertEquals(this.dataModel1.getUuid(), engineDataModel.getInteractions().get(0).getUuid());
        Assert.assertEquals(this.dataModel2.getUuid(), engineDataModel.getInteractions().get(1).getUuid());
        Assert.assertEquals(interactionDataModel.getUuid(), engineDataModel.getInteractions().get(2).getUuid());
        Assert.assertEquals(interactionDataModel.getNextInteractionUuid(), engineDataModel.getInteractions().get(2).getNext().getUuid());
        Assert.assertEquals(this.dataModel1.getUuid(), engineDataModel.getInteractions().get(2).getNext().getUuid());
    }
}
