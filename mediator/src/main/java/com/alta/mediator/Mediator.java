package com.alta.mediator;

import com.alta.dao.DaoInjectorModule;
import com.alta.dao.data.characterPreservation.CharacterPreservationModel;
import com.alta.dao.domain.characterPreservation.CharacterPreservationService;
import com.alta.engine.Engine;
import com.alta.engine.EngineInjectorModule;
import com.alta.mediator.configuration.MediatorConfiguration;
import com.alta.mediator.domain.frameStage.FrameStageDataProvider;
import com.alta.scene.SceneInjectorModule;
import com.alta.utils.ExecutorServiceFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * Provides the mediator that handles events from modules
 */
@Slf4j
public class Mediator {

    private static final String ENGINE_THREAD_POOL_NAME = "engine-main-thread";

    private final FrameStageDataProvider frameStageDataProvider;
    private final ExecutorService engineMainThread;
    private final Engine engine;
    private CharacterPreservationModel characterPreservationModel;

    /**
     * Initialize new instance of {@link Mediator}
     */
    public Mediator() {
        Injector injector = Guice.createInjector(
                new MediatorInjectorModule(),
                new DaoInjectorModule(),
                new EngineInjectorModule(),
                new SceneInjectorModule()
        );

        this.frameStageDataProvider = injector.getInstance(FrameStageDataProvider.class);

        this.engine = injector.getInstance(Engine.class);

        this.engineMainThread = ExecutorServiceFactory.create(1, ENGINE_THREAD_POOL_NAME);
        this.characterPreservationModel = injector.getInstance(CharacterPreservationService.class).getCharacterPreservation(1);

        injector.getInstance(MediatorConfiguration.class).configure();
    }

    /**
     * Starts the scene
     */
    public void loadSavedGameAndStart() {
        this.engineMainThread.execute(() -> {
            this.engine.tryToRenderFrameStage(this.frameStageDataProvider.getFromPreservation(this.characterPreservationModel));
            this.engine.startScene();
        });
    }
}
