package com.alta.mediator.command.frameStage;

import com.alta.engine.Engine;
import com.alta.engine.model.FrameStageDataModel;
import com.alta.engine.model.InteractionDataModel;
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
    private final FrameStageDataModel frameStageDataModel;
    private final InteractionDataModel interactionDataModel;

    /**
     * Initialize new instance of {@link RenderFrameStageCommand}.
     */
    @AssistedInject
    public RenderFrameStageCommand(Engine engine,
                                   @Assisted FrameStageDataModel data,
                                   @Assisted InteractionDataModel interactionDataModel) {
        this.engine = engine;
        this.frameStageDataModel = data;
        this.interactionDataModel = interactionDataModel;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        if (this.frameStageDataModel == null) {
            log.error("The data of frame stage is required for rendering");
            return;
        }

        log.info("Try to render map: '{}'", this.frameStageDataModel.getMapName());
        this.engine.tryToRenderFrameStage(this.frameStageDataModel, this.interactionDataModel);
        log.info("Rendering of map: '{}' completed", this.frameStageDataModel.getMapName());
    }
}
