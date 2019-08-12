package com.alta.mediator.domain.actor;

import com.alta.behaviorprocess.data.common.FaceSetDescription;
import com.alta.dao.data.actor.ActorModel;
import com.alta.dao.domain.actor.ActorService;
import com.alta.dao.domain.map.internalEntities.AlterableNpcEntity;
import com.alta.dao.domain.map.internalEntities.NpcEntity;
import com.alta.dao.domain.map.internalEntities.SimpleNpcEntity;
import com.alta.engine.data.frameStage.ActingCharacterEngineModel;
import com.alta.engine.data.frameStage.NpcEngineModel;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.stream.Collectors;

/**
 * Provides the service that manipulated model related to {@link com.alta.scene.entities.Actor}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ActorDataProviderImpl implements ActorDataProvider {

    private final ActorService actorService;
    private final ActorEngineMapper actorEngineMapper;

    /**
     * Gets the acting character by given skin name
     *
     * @param skinName - the name of skin for character
     * @param startCoordinates - the coordinates of start position for actor
     * @return the {@link ActingCharacterEngineModel}
     */
    @Override
    public ActingCharacterEngineModel getActingCharacter(String skinName, Point startCoordinates, String uuid) {
        ActorModel actorModel = this.actorService.getActorModel(skinName);
        actorModel.setStartMapCoordinates(startCoordinates);
        actorModel.setUuid(uuid);

        return this.actorEngineMapper.doMappingForActingCharacter(actorModel, skinName);
    }

    /**
     * Gets the simple npcMovementProcessor by given npcMovementProcessor entity.
     *
     * @param npcEntity - the npcMovementProcessor entity to create engine model.
     * @return the {@link NpcEngineModel}
     */
    @Override
    public NpcEngineModel getSimpleNpc(SimpleNpcEntity npcEntity) {
        return this.createNpcEngineModelFromNpcEntity(npcEntity);
    }

    /**
     * Gets the alterable npcMovementProcessor by given npcMovementProcessor entity.
     *
     * @param npcEntity - the npcMovementProcessor entity to create engine model.
     * @return the {@link NpcEngineModel} instance.
     */
    @Override
    public NpcEngineModel getAlterableNpc(AlterableNpcEntity npcEntity) {
        NpcEngineModel npcEngineModel = this.createNpcEngineModelFromNpcEntity(npcEntity);
        if (npcEntity.getMovementRules() == null) {
            log.error("The movement rules is required for the AlterableNpcEntity: {}", npcEntity.getUuid());
            return npcEngineModel;
        }

        npcEngineModel.setMovementRouteLooped(npcEntity.getMovementRules().isLooped());
        npcEngineModel.setRouteDescription(
        npcEntity.getMovementRules().getRouteDescription()
                .stream()
                .map(rd -> new NpcEngineModel.RouteDescription(rd.getX(), rd.getY(), rd.getFinalDirection()))
                .collect(Collectors.toList())
        );

        return npcEngineModel;
    }

    @Override
    public FaceSetDescription getFaceSetForActor(String actorName) {
        ActorModel actorModel = this.actorService.getActorModel(actorName);
        if (actorModel == null) {
            log.debug("Actor with given name not found {}", actorName);
            return null;
        }

        return this.actorEngineMapper.doMappingForFaceSetDescriptor(actorModel);
    }

    private NpcEngineModel createNpcEngineModelFromNpcEntity(NpcEntity npcEntity) {
        ActorModel actorModel = this.actorService.getActorModel(npcEntity.getName());
        actorModel.setRepeatingMovementDurationTime(actorModel.getRepeatingMovementDurationTime());
        actorModel.setRepeatingMovementDurationTime(npcEntity.getRepeatingMovementDurationTime());
        actorModel.setStartMapCoordinates(new Point(npcEntity.getStartX(), npcEntity.getStartY()));
        actorModel.setUuid(npcEntity.getUuid());

        NpcEngineModel npcEngineModel = this.actorEngineMapper.doMappingForSimpleNpc(actorModel);
        npcEngineModel.setAnimatedAlways(npcEntity.isAnimatedAlways());
        npcEngineModel.setInitialDirection(npcEntity.getInitialDirection());
        npcEngineModel.setMovementStrategy(npcEntity.getMovementStrategy());
        npcEngineModel.setMovementSpeed(npcEntity.getMovementSpeed());

        return npcEngineModel;
    }
}
