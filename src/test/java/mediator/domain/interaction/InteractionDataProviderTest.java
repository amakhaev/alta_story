package mediator.domain.interaction;

import com.alta.behaviorprocess.data.interaction.InteractionModel;
import com.alta.dao.data.interaction.InteractionDataModel;
import com.alta.dao.data.common.effect.visible.DialogueEffectDataModel;
import com.alta.dao.data.common.effect.visible.HideFacilityEffectDataModel;
import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.common.effect.visible.ShowFacilityEffectDataModel;
import com.alta.dao.domain.interaction.InteractionService;
import com.alta.mediator.domain.effect.EffectDataProvider;
import com.alta.mediator.domain.interaction.InteractionConditionService;
import com.alta.mediator.domain.interaction.InteractionDataProvider;
import com.alta.mediator.domain.interaction.InteractionDataProviderImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InteractionDataProviderTest {

    private InteractionService interactionService;
    private InteractionDataProvider interactionDataProvider;
    private EffectDataProvider effectDataProvider;
    private InteractionDataModel dataModel1;
    private InteractionDataModel dataModel2;

    @Before
    public void setUp() {
        this.interactionService = mock(InteractionService.class);
        this.effectDataProvider = mock(EffectDataProvider.class);
        this.interactionDataProvider = new InteractionDataProviderImpl(
                this.interactionService, mock(InteractionConditionService.class), this.effectDataProvider
        );

        EffectDataModel dialogueEffect = DialogueEffectDataModel.builder()
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
        when(this.interactionService.getInteractions(anyString(), anyString(), anyInt())).thenReturn(
                Collections.singletonMap(this.dataModel1.getUuid(), this.dataModel1)
        );

        InteractionModel interactionModel = this.interactionDataProvider.getInteractionByRelatedMapName(
                "relatedMap", this.dataModel1.getTargetUuid(), 5, Collections.emptyList()
        );

        Assert.assertEquals(this.dataModel1.getUuid(), interactionModel.getUuid());
        Assert.assertEquals(this.dataModel1.getTargetUuid(), interactionModel.getTargetUuid());
        Assert.assertNull(interactionModel.getNext());
        Assert.assertEquals(0, interactionModel.getFailedPreConditionInteractionEffects().size());
        Assert.assertEquals(0, interactionModel.getShiftTiles().size());
        Assert.assertNull(interactionModel.getPreCondition());
        Assert.assertFalse(interactionModel.isCompleted());
    }

    @Test
    public void interactionDataProvider_oneInteractionWithInvalidChapter_returnEmptyList() {
        when(this.interactionService.getInteractions(anyString())).thenReturn(Collections.singletonList(this.dataModel1));

        InteractionModel interactionModel = this.interactionDataProvider.getInteractionByRelatedMapName(
                "relatedMap", this.dataModel1.getTargetUuid(), 15, Collections.emptyList()
        );

        Assert.assertNull(interactionModel);
    }

    @Test
    public void interactionDataProvider_twoInteraction_returnOne() {
        Map<String, InteractionDataModel> models = new HashMap<>();
        models.put(this.dataModel1.getUuid(), this.dataModel1);
        models.put(this.dataModel2.getUuid(), this.dataModel2);

        when(this.interactionService.getInteractions(anyString(), anyString(), anyInt())).thenReturn(models);

        InteractionModel interactionModel = this.interactionDataProvider.getInteractionByRelatedMapName(
                "relatedMap", this.dataModel1.getTargetUuid(), 12, Collections.emptyList()
        );

        Assert.assertEquals(this.dataModel1.getUuid(), interactionModel.getUuid());
        Assert.assertEquals(this.dataModel1.getTargetUuid(), interactionModel.getTargetUuid());
        Assert.assertNull(interactionModel.getNext());
        Assert.assertEquals(0, interactionModel.getFailedPreConditionInteractionEffects().size());
        Assert.assertEquals(0, interactionModel.getShiftTiles().size());
        Assert.assertNull(interactionModel.getPreCondition());
        Assert.assertFalse(interactionModel.isCompleted());
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

        Map<String, InteractionDataModel> models = new HashMap<>();
        models.put(this.dataModel1.getUuid(), this.dataModel1);
        models.put(this.dataModel2.getUuid(), this.dataModel2);
        models.put(interactionDataModel.getUuid(), interactionDataModel);

        when(this.interactionService.getInteractions(anyString(), anyString(), anyInt())).thenReturn(models);

        InteractionModel interactionModel = this.interactionDataProvider.getInteractionByRelatedMapName(
                "relatedMap", this.dataModel1.getTargetUuid(), 10, Collections.emptyList()
        );

        Assert.assertEquals(interactionDataModel.getUuid(), interactionModel.getUuid());
        Assert.assertEquals(this.dataModel1.getUuid(), interactionModel.getNext().getUuid());
    }
}
