package com.alta.engine.dataFactory;

import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.customException.EngineException;
import com.alta.engine.sceneComponent.actor.ActingCharacterEngineModel;
import com.alta.engine.sceneComponent.facility.FacilityEngineModel;
import com.alta.engine.sceneComponent.actor.SimpleNpcEngineModel;
import com.alta.engine.sceneComponent.actor.ActorCharacterComponent;
import com.alta.engine.sceneComponent.facility.FacilityComponent;
import com.alta.engine.sceneComponent.frameStage.FrameStageComponent;
import com.alta.engine.sceneComponent.frameTemplate.FrameTemplateComponent;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import com.google.inject.Singleton;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the factory that creates the entities related to scene
 */
@Slf4j
public class SceneFrameStageProvider {

    /**
     * Creates the FrameStage instance by given data
     *
     * @param data - the data that full describes the frame stage
     * @param stageComputator - the computator of frame stage
     * @param asyncTaskManager - the manager of async tasks
     * @return created {@link FrameStageComponent} instance based of @param data
     */
    @Builder
    public static FrameStageComponent createFrameStage(FrameStageData data,
                                                StageComputator stageComputator,
                                                AsyncTaskManager asyncTaskManager) {
        validateFrameStageData(data);

        List<ActorCharacterComponent> actorCharacters = createActors(data.getActingCharacter(), data.getSimpleNpc());
        log.info("Creating of actors completed. Count: {}", actorCharacters.size());

        FrameStageComponent frameStageComponent = new FrameStageComponent(
                createFrameTemplate(data.getTiledMapAbsolutePath()),
                actorCharacters,
                createStageFacilities(data.getFacilities()),
                stageComputator,
                asyncTaskManager
        );
        log.debug("Completed creating FrameStageComponent with map: {}", data.getTiledMapAbsolutePath());
        return frameStageComponent;
    }

    /**
     * Creates the frame template by given path to tile map
     *
     * @param tiledMapPath - the absolute path to tiled map
     * @return the {@link FrameTemplateComponent} instance.
     */
    private static FrameTemplateComponent createFrameTemplate(String tiledMapPath) {
        return new FrameTemplateComponent(tiledMapPath);
    }

    /**
     * Creates the list of facilities that available on scene
     *
     * @param facilityEngineModels - the facilities to be created on scene
     * @return the {@link List< FacilityComponent >} instance.
     */
    private static List<FacilityComponent> createStageFacilities(List<FacilityEngineModel> facilityEngineModels) {
        if (facilityEngineModels == null) {
            return Collections.emptyList();
        }

        return facilityEngineModels.parallelStream()
                .map(facilityModel -> new FacilityComponent(
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
     * @return the {@link List< ActorCharacterComponent >} instance.
     */
    private static List<ActorCharacterComponent> createActors(ActingCharacterEngineModel actingCharacter, List<SimpleNpcEngineModel> simpleNpc) {
        List<ActorCharacterComponent> actorCharacterComponents = new ArrayList<>();
        if (actingCharacter != null) {
            actorCharacterComponents.add(
                    createActorCharacter(actingCharacter.getAnimationDescriptors(), actingCharacter.getUuid())
            );
        }

        if (simpleNpc != null) {
            actorCharacterComponents.addAll(createSimpleNpcList(simpleNpc));
        }

        return actorCharacterComponents;
    }

    /**
     * Creates the simple npc entities
     *
     * @param simpleNpcEngineModels - the models to create scee compionent
     * @return the {@link List< ActorCharacterComponent >} instance.
     */
    private static List<ActorCharacterComponent> createSimpleNpcList(List<SimpleNpcEngineModel> simpleNpcEngineModels) {
        if (simpleNpcEngineModels == null) {
            return Collections.emptyList();
        }

        return simpleNpcEngineModels.parallelStream()
                .map(simpleNpcEngineModel -> createActorCharacter(
                        simpleNpcEngineModel.getAnimationDescriptors(), simpleNpcEngineModel.getUuid()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Creates the actor character on scene.
     *
     * @param animationDescriptors - the list of descriptors that available for acting character
     * @param uuid - the uuid of actor
     * @return created {@link ActorCharacterComponent} instance.
     */
    private static ActorCharacterComponent createActorCharacter(List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors,
                                                         String uuid) {
        return new ActorCharacterComponent(animationDescriptors, uuid);
    }

    private static void validateFrameStageData(FrameStageData data) throws EngineException {
        if (data == null) {
            throw new EngineException("The FrameStageData is null. It required for creating frame stage.");
        }

        if (data.getFocusPointMapStartPosition() == null) {
            throw new EngineException("The focus point is required for creating frame stage.");
        }

        if (data.getActingCharacter() == null) {
            throw new EngineException("The acting character not present for stage with path");
        }

        if (data.getActingCharacter().getAnimationDescriptors() == null ||
                data.getActingCharacter().getAnimationDescriptors().isEmpty()) {
            throw new EngineException("The acting character doesn't contains descriptors for animation");
        }
    }
}
