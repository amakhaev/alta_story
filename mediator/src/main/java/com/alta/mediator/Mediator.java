package com.alta.mediator;

import com.alta.dao.DaoInjectorModule;
import com.alta.dao.data.facility.FacilityModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.engine.Engine;
import com.alta.engine.EngineInjectorModule;
import com.alta.engine.SceneProxy;
import com.alta.mediator.domain.frameStage.FrameStageService;
import com.alta.utils.ThreadPoolExecutor;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Provides the mediator that handles events from modules
 */
public class Mediator {

    private static final String ENGINE_THREAD_POOL_NAME = "engine-main-thread";

    private final FrameStageService frameStageService;
    private final ThreadPoolExecutor engineMainThread;
    private final Engine engine;

    /**
     * Initialize new instance of {@link Mediator}
     */
    public Mediator() {
        Injector injector = Guice.createInjector(
                new MediatorInjectorModule(),
                new DaoInjectorModule(),
                new EngineInjectorModule()
        );

        this.frameStageService = injector.getInstance(FrameStageService.class);
        this.engine = injector.getInstance(Engine.class);
        this.engineMainThread = new ThreadPoolExecutor(1, ENGINE_THREAD_POOL_NAME);
    }

    /**
     * Starts the scene
     */
    public void loadSavedGameAndStart() {
        this.engineMainThread.run(() -> {
            this.engine.tryToRenderFrameStage(this.frameStageService.getFromPreservation());
            this.engine.startScene();
        });
    }
}
