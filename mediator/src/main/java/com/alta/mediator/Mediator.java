package com.alta.mediator;

import com.alta.engine.Engine;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.frameStage.FrameStageCommandFactory;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import com.alta.utils.ExecutorServiceFactory;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * Provides the mediator that handles events from modules
 */
@Slf4j
public class Mediator {

    private static final String ENGINE_THREAD_POOL_NAME = "engine-main-thread";

    private final ExecutorService engineMainThread;
    private final Engine engine;
    private final FrameStageCommandFactory frameStageCommandFactory;
    private final PreservationCommandFactory preservationCommandFactory;
    private final CommandExecutor commandExecutor;

    /**
     * Initialize new instance of {@link Mediator}
     */
    @Inject
    public Mediator(Engine engine,
                    CommandExecutor commandExecutor,
                    FrameStageCommandFactory frameStageCommandFactory,
                    PreservationCommandFactory preservationCommandFactory) {
        this.engine = engine;
        this.commandExecutor = commandExecutor;
        this.frameStageCommandFactory = frameStageCommandFactory;
        this.preservationCommandFactory = preservationCommandFactory;

        this.engineMainThread = ExecutorServiceFactory.create(1, ENGINE_THREAD_POOL_NAME);
    }

    /**
     * Starts the scene
     */
    public void loadSavedGameAndStart() {
        this.engineMainThread.execute(() -> {
            this.commandExecutor.executeCommand(this.preservationCommandFactory.createClearTemporaryPreservationDataCommand());
            this.engine.runInitialSync();

            this.commandExecutor.executeCommand(this.frameStageCommandFactory.createRenderFrameStageFromPreservationCommand());
            this.engine.startScene();
        });
    }
}
