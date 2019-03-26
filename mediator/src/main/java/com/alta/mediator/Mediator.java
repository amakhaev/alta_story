package com.alta.mediator;

import com.alta.dao.DaoInjectorModule;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.engine.Engine;
import com.alta.engine.EngineInjectorModule;
import com.alta.mediator.domain.frameStage.FrameStageDataProvider;
import com.alta.utils.ExecutorServiceFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
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
    private PreservationModel preservationModel;

    /**
     * Initialize new instance of {@link Mediator}
     */
    public Mediator() {
        Injector injector = Guice.createInjector(
                new MediatorInjectorModule(),
                new DaoInjectorModule(),
                new EngineInjectorModule()
        );

        this.frameStageDataProvider = injector.getInstance(FrameStageDataProvider.class);

        this.engine = injector.getInstance(Engine.class);
        this.engine.setEngineListener(this::loadAndRender);

        this.engineMainThread = ExecutorServiceFactory.create(2, ENGINE_THREAD_POOL_NAME);
        this.preservationModel = injector.getInstance(PreservationService.class).getPreservation();
    }

    /**
     * Starts the scene
     */
    public void loadSavedGameAndStart() {
        this.engineMainThread.execute(() -> {
            this.engine.tryToRenderFrameStage(this.frameStageDataProvider.getFromPreservation(this.preservationModel));
            this.engine.startScene();
        });
    }

    private void loadAndRender(String mapName, Point startCoordinates) {
        log.info("Try to load and render map: {}", mapName);
        this.engine.tryToRenderFrameStage(
                this.frameStageDataProvider.getByParams(
                        mapName,
                        "person1",
                        startCoordinates
                )
        );
    }
}
