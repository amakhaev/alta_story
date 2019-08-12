package mediator.domain.effect;

import com.alta.behaviorprocess.data.effect.*;
import com.alta.computator.service.computator.movement.MovementWorkerImpl;
import com.alta.dao.data.common.effect.visible.DialogueEffectDataModel;
import com.alta.dao.data.common.effect.visible.HideFacilityEffectDataModel;
import com.alta.dao.data.common.effect.visible.RouteMovementEffectDataModel;
import com.alta.dao.data.common.effect.visible.ShowFacilityEffectDataModel;
import com.alta.dao.domain.actor.ActorService;
import com.alta.mediator.domain.actor.ActorEngineMapper;
import com.alta.mediator.domain.effect.EffectDataProvider;
import com.alta.mediator.domain.effect.EffectDataProviderImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class EffectDataProviderTest {

    private EffectDataProvider effectDataProvider;
    private ActorService actorService;
    private ActorEngineMapper actorEngineMapper;

    @Before
    public void setUp() {
        this.actorEngineMapper = mock(ActorEngineMapper.class);
        this.actorService = mock(ActorService.class);

        this.effectDataProvider = new EffectDataProviderImpl(this.actorService, this.actorEngineMapper);
    }

    @Test
    public void effectDataProvider_createDialogEffect_effectCreated() {
        DialogueEffectDataModel dataModel = DialogueEffectDataModel.builder()
                .text("test text")
                .speakerUuid("uuid")
                .build();

        List<EffectModel> effects = this.effectDataProvider.getEffects(Collections.singletonList(dataModel));

        verify(this.actorService, times(0)).getActorModel(anyString());
        verify(this.actorEngineMapper, times(0)).doMappingForFaceSetDescriptor(any());
        Assert.assertEquals(1, effects.size());
        Assert.assertEquals(EffectModel.EffectType.DIALOGUE, effects.get(0).getType());
        Assert.assertEquals(dataModel.getText(), ((DialogueEffectModel) effects.get(0)).getText());
        Assert.assertNotNull(((DialogueEffectModel) effects.get(0)).getDialogueSpeaker());
    }

    @Test
    public void effectDataProvider_createHideFacilityEffect_effectCreated() {
        HideFacilityEffectDataModel dataModel = new HideFacilityEffectDataModel();
        dataModel.setFacilityUuid("uuid");

        List<EffectModel> effects = this.effectDataProvider.getEffects(Collections.singletonList(dataModel));

        Assert.assertEquals(1, effects.size());
        Assert.assertEquals(EffectModel.EffectType.HIDE_FACILITY, effects.get(0).getType());
        Assert.assertEquals(dataModel.getFacilityUuid(), ((HideFacilityEffectModel) effects.get(0)).getFacilityUuid());
    }

    @Test
    public void effectDataProvider_createShowFacilityEffect_effectCreated() {
        ShowFacilityEffectDataModel dataModel = new ShowFacilityEffectDataModel();
        dataModel.setFacilityUuid("uuid");

        List<EffectModel> effects = this.effectDataProvider.getEffects(Collections.singletonList(dataModel));

        Assert.assertEquals(1, effects.size());
        Assert.assertEquals(EffectModel.EffectType.SHOW_FACILITY, effects.get(0).getType());
        Assert.assertEquals(dataModel.getFacilityUuid(), ((ShowFacilityEffectModel) effects.get(0)).getFacilityUuid());
    }

    @Test
    public void effectDataProvider_createRouteMovementEffect_effectCreated() {
        RouteMovementEffectDataModel dataModel = RouteMovementEffectDataModel.builder()
                .x(10)
                .y(10)
                .finalDirection("UP")
                .movementSpeed("SLOW")
                .targetUuid("uuid")
                .build();

        List<EffectModel> effects = this.effectDataProvider.getEffects(Collections.singletonList(dataModel));

        Assert.assertEquals(1, effects.size());
        Assert.assertEquals(EffectModel.EffectType.ROUTE_MOVEMENT, effects.get(0).getType());
        Assert.assertEquals(dataModel.getTargetUuid(), ((RouteMovementEffectModel) effects.get(0)).getTargetUuid());
        Assert.assertEquals(dataModel.getFinalDirection(), ((RouteMovementEffectModel) effects.get(0)).getFinalDirection());
        Assert.assertEquals(MovementWorkerImpl.SLOW_MOVE_SPEED, ((RouteMovementEffectModel) effects.get(0)).getMovementSpeed());
        Assert.assertEquals(dataModel.getX(), ((RouteMovementEffectModel) effects.get(0)).getX());
        Assert.assertEquals(dataModel.getY(), ((RouteMovementEffectModel) effects.get(0)).getY());
    }
}
