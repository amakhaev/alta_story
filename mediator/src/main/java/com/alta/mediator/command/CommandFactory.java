package com.alta.mediator.command;

import com.alta.engine.utils.dataBuilder.FrameStageData;

/**
 * Provides the factory to create commands.
 */
public interface CommandFactory {

    /**
     * Creates the {@link RenderFrameStageCommand} instance.
     *
     * @param data - the data for creating.
     * @return the {@link RenderFrameStageCommand} instance.
     */
    RenderFrameStageCommand createRenderFrameStageCommand(FrameStageData data);

}
