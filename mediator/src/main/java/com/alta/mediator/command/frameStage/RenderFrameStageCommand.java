package com.alta.mediator.command.frameStage;

import com.alta.engine.Engine;
import com.alta.engine.model.FrameStageEngineDataModel;
import com.alta.engine.model.InteractionEngineDataModel;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the command that rendered frame stage.
 */
@Slf4j
public class RenderFrameStageCommand implements Command {

    private final Engine engine;
    private final FrameStageEngineDataModel frameStageEngineDataModel;
    private final InteractionEngineDataModel interactionEngineDataModel;

    /**
     * Initialize new instance of {@link RenderFrameStageCommand}.
     */
    @AssistedInject
    public RenderFrameStageCommand(Engine engine,
                                   @Assisted FrameStageEngineDataModel data,
                                   @Assisted InteractionEngineDataModel interactionEngineDataModel) {
        this.engine = engine;
        this.frameStageEngineDataModel = data;
        this.interactionEngineDataModel = interactionEngineDataModel;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        if (this.frameStageEngineDataModel == null) {
            log.error("The data of frame stage is required for rendering");
            return;
        }

        log.info("Try to render map: '{}'", this.frameStageEngineDataModel.getMapName());
        this.engine.tryToRenderFrameStage(this.frameStageEngineDataModel, this.interactionEngineDataModel);
        log.info("Rendering of map: '{}' completed", this.frameStageEngineDataModel.getMapName());
    }
}
