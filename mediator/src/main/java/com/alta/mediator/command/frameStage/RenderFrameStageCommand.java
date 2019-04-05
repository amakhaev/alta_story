package com.alta.mediator.command.frameStage;

import com.alta.engine.Engine;
import com.alta.engine.utils.dataBuilder.FrameStageData;
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
    private final FrameStageData frameStageData;

    /**
     * Initialize new instance of {@link RenderFrameStageCommand}.
     */
    @AssistedInject
    public RenderFrameStageCommand(Engine engine,  @Assisted FrameStageData data) {
        this.engine = engine;
        this.frameStageData = data;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        if (this.frameStageData == null) {
            log.error("The data of frame stage is required for rendering");
            return;
        }

        log.info("Try to render map: '{}'", this.frameStageData.getMapName());
        this.engine.tryToRenderFrameStage(this.frameStageData);
        log.info("Rendering of map: '{}' completed", this.frameStageData.getMapName());
    }
}
