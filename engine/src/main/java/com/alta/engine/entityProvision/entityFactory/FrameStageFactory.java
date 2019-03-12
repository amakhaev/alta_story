package com.alta.engine.entityProvision.entityFactory;

import com.alta.computator.service.stage.StageComputator;
import com.alta.engine.customException.EngineException;
import com.alta.engine.entityProvision.entities.BaseActorCharacter;
import com.alta.engine.entityProvision.entities.BaseFrameStage;
import com.alta.engine.inputListener.ActionProducer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Provides the factory to generate entities
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrameStageFactory {

    private final SceneFrameStageFactory sceneFrameStageFactory;
    private final ComputatorFrameStageFactory computatorFrameStageFactory;
    private final ActionProducer actionProducer;

    /**
     * Creates the FrameStage instance by given data
     *
     * @param data - the data that full describes the frame stage
     * @return created {@link BaseFrameStage} instance based of @param data
     */
    public BaseFrameStage createFrameStage(FrameStageData data) throws EngineException {
        this.validateFrameStageData(data);

        log.debug("Started creating BaseFrameStage with path to map: {}", data.getTiledMapAbsolutePath());
        StageComputator stageComputator = this.computatorFrameStageFactory.createStageComputator(
                data.getFocusPointMapStartPosition(),
                data.getActingCharacter(),
                data.getFacilities(),
                data.getSimpleNpc()
        );
        log.info("Creating of StageComputator completed.");

        List<BaseActorCharacter> actorCharacters = this.sceneFrameStageFactory.createActors(
                data.getActingCharacter(),
                data.getSimpleNpc()
        );

        log.info("Creating of actors completed. Count: {}", actorCharacters.size());

        BaseFrameStage baseFrameStage = new BaseFrameStage(
                this.sceneFrameStageFactory.createFrameTemplate(data.getTiledMapAbsolutePath()),
                actorCharacters,
                this.sceneFrameStageFactory.createStageFacilities(data.getFacilities()),
                stageComputator,
                this.actionProducer
        );
        log.debug("Completed creating BaseFrameStage with map: {}", data.getTiledMapAbsolutePath());
        return baseFrameStage;
    }

    private void validateFrameStageData(FrameStageData data) throws EngineException {
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
