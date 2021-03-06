package com.alta.engine.view.componentProvider;

import com.alta.computator.Computator;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.core.customException.EngineException;
import com.alta.engine.data.FrameStageEngineDataModel;
import com.alta.engine.data.frameStage.ActingCharacterEngineModel;
import com.alta.engine.data.frameStage.FacilityEngineModel;
import com.alta.engine.data.frameStage.NpcEngineModel;
import com.alta.engine.view.components.actor.ActorCharacterComponent;
import com.alta.engine.view.components.facility.FacilityComponent;
import com.alta.engine.view.components.frameStage.FrameStageComponent;
import com.alta.engine.view.components.frameStage.FrameStageComponentFactory;
import com.alta.engine.view.components.frameTemplate.FrameTemplateComponent;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the factory that creates the entities related to scene
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrameStageComponentProvider {

    private final FrameStageComponentFactory componentFactory;

    /**
     * Creates the FrameStage instance by given model
     *
     * @param data - the model that full describes the frame stage
     * @param computator - the {@link Computator} instance.
     * @return created {@link FrameStageComponent} instance based of @param model
     */
    public FrameStageComponent createFrameStage(FrameStageEngineDataModel data, Computator computator) {
        validateFrameStageData(data);

        List<ActorCharacterComponent> actorCharacters = createActors(data.getActingCharacter(), data.getNpcList());
        log.info("Creating of actors completed. Count: {}", actorCharacters.size());

        FrameStageComponent frameStageComponent = this.componentFactory.createFrameStage(
                createFrameTemplate(data.getTiledMapAbsolutePath()),
                actorCharacters,
                createStageFacilities(data.getFacilities().stream().filter(FacilityEngineModel::isVisible).collect(Collectors.toList())),
                computator
        );
        log.debug("Completed creating FrameStageComponent with map: {}", data.getTiledMapAbsolutePath());
        return frameStageComponent;
    }

    /**
     * Creates the facility component from engine model.
     *
     * @param facilityModel - the engine model to be used for creating component.
     * @return the created {@link FacilityComponent} instance.
     */
    public FacilityComponent createFacilityComponent(FacilityEngineModel facilityModel) {
        return new FacilityComponent(
                facilityModel.getUuid(),
                facilityModel.getPathToImageSet(),
                facilityModel.getTileWidth(),
                facilityModel.getTileHeight()
        );
    }

    /**
     * Creates the frame template by given path to tile map
     *
     * @param tiledMapPath - the absolute path to tiled map
     * @return the {@link FrameTemplateComponent} instance.
     */
    private FrameTemplateComponent createFrameTemplate(String tiledMapPath) {
        return new FrameTemplateComponent(tiledMapPath);
    }

    /**
     * Creates the list of facilities that available on scene
     *
     * @param facilityEngineModels - the facilities to be created on scene
     * @return the {@link List< FacilityComponent >} instance.
     */
    private List<FacilityComponent> createStageFacilities(List<FacilityEngineModel> facilityEngineModels) {
        if (facilityEngineModels == null) {
            return Collections.emptyList();
        }

        return facilityEngineModels.parallelStream()
                .map(this::createFacilityComponent)
                .collect(Collectors.toList());
    }

    /**
     * Creates the list of actors that available for scene
     *
     * @param actingCharacter - the acting character on scene
     * @param simpleNpc - the list of simple npcMovement
     * @return the {@link List< ActorCharacterComponent >} instance.
     */
    private List<ActorCharacterComponent> createActors(ActingCharacterEngineModel actingCharacter, List<NpcEngineModel> simpleNpc) {
        List<ActorCharacterComponent> actorCharacterComponents = new ArrayList<>();
        if (actingCharacter != null) {
            actorCharacterComponents.add(
                    createActorCharacter(actingCharacter.getAnimationDescriptors(), actingCharacter.getUuid(), false)
            );
        }

        if (simpleNpc != null) {
            actorCharacterComponents.addAll(createSimpleNpcList(simpleNpc));
        }

        return actorCharacterComponents;
    }

    /**
     * Creates the simple npcMovement entities
     *
     * @param npcEngineModels - the models to create scee compionent
     * @return the {@link List< ActorCharacterComponent >} instance.
     */
    private List<ActorCharacterComponent> createSimpleNpcList(List<NpcEngineModel> npcEngineModels) {
        if (npcEngineModels == null) {
            return Collections.emptyList();
        }

        return npcEngineModels.parallelStream()
                .map(simpleNpcEngineModel -> createActorCharacter(
                        simpleNpcEngineModel.getAnimationDescriptors(),
                        simpleNpcEngineModel.getUuid(),
                        simpleNpcEngineModel.isAnimatedAlways()
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
    private ActorCharacterComponent createActorCharacter(List<ActorAnimationDescriptor<MovementDirection>> animationDescriptors,
                                                         String uuid,
                                                         boolean isAnimatedAlways) {
        return new ActorCharacterComponent(animationDescriptors, uuid, isAnimatedAlways);
    }

    private void validateFrameStageData(FrameStageEngineDataModel data) throws EngineException {
        if (data == null) {
            throw new EngineException("The FrameStageEngineDataModel is null. It required for creating frame stage.");
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
