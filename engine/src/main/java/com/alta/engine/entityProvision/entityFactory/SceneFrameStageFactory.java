package com.alta.engine.entityProvision.entityFactory;

import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.engine.data.ActingCharacterEngineModel;
import com.alta.engine.data.FacilityEngineModel;
import com.alta.engine.data.SimpleNpcEngineModel;
import com.alta.engine.entityProvision.entities.BaseActorCharacter;
import com.alta.engine.entityProvision.entities.BaseFacility;
import com.alta.engine.entityProvision.entities.BaseFrameTemplate;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the factory that creates the entities related to scene
 */
@Singleton
class SceneFrameStageFactory {

    /**
     * Creates the frame template by given path to tile map
     *
     * @param tiledMapPath - the absolute path to tiled map
     * @return the {@link BaseFrameTemplate} instance.
     */
    BaseFrameTemplate createFrameTemplate(String tiledMapPath) {
        return new BaseFrameTemplate(tiledMapPath);
    }

    /**
     * Creates the list of facilities that available on scene
     *
     * @param facilityEngineModels - the facilities to be created on scene
     * @return the {@link List<BaseFacility>} instance.
     */
    List<BaseFacility> createStageFacilities(List<FacilityEngineModel> facilityEngineModels) {
        if (facilityEngineModels == null) {
            return Collections.emptyList();
        }

        return facilityEngineModels.parallelStream()
                .map(facilityModel -> new BaseFacility(
                        facilityModel.getUuid(),
                        facilityModel.getPathToImageSet(),
                        facilityModel.getTileWidth(),
                        facilityModel.getTileHeight())
                )
                .collect(Collectors.toList());
    }

    /**
     * Creates the list of actors that available for scene
     *
     * @param actingCharacter - the acting character on scene
     * @param simpleNpc - the list of simple npc
     * @return the {@link List<BaseActorCharacter>} instance.
     */
    List<BaseActorCharacter> createActors(ActingCharacterEngineModel actingCharacter, List<SimpleNpcEngineModel> simpleNpc) {
        List<BaseActorCharacter> baseActorCharacters = new ArrayList<>();
        if (actingCharacter != null) {
            baseActorCharacters.add(
                    this.createActorCharacter(actingCharacter.getAnimationDescriptors(), actingCharacter.getUuid())
            );
        }

        if (simpleNpc != null) {
            baseActorCharacters.addAll(this.createSimpleNpcList(simpleNpc));
        }

        return baseActorCharacters;
    }

    /**
     * Creates the simple npc entities
     *
     * @param simpleNpcEngineModels - the models to create scee compionent
     * @return the {@link List<BaseActorCharacter>} instance.
     */
    private List<BaseActorCharacter> createSimpleNpcList(List<SimpleNpcEngineModel> simpleNpcEngineModels) {
        if (simpleNpcEngineModels == null) {
            return Collections.emptyList();
        }

        return simpleNpcEngineModels.parallelStream()
                .map(simpleNpcEngineModel -> this.createActorCharacter(
                        simpleNpcEngineModel.getAnimationDescriptors(), simpleNpcEngineModel.getUuid()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Creates the actor character on scene.
     *
     * @param animationDescriptors - the list of descriptors that available for acting character
     * @param uuid - the uuid of actor
     * @return created {@link BaseActorCharacter} instance.
     */
    private BaseActorCharacter createActorCharacter(List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors,
                                                    String uuid) {
        return new BaseActorCharacter(animationDescriptors, uuid);
    }
}
