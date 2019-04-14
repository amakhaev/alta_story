package com.alta.mediator.command.frameStage;

import com.alta.engine.model.FrameStageDataModel;
import com.alta.engine.model.InteractionDataModel;
import com.google.inject.assistedinject.Assisted;

import java.awt.*;

/**
 * Provides the factory to create commands.
 */
public interface FrameStageCommandFactory {

    /**
     * Creates the {@link RenderFrameStageCommand} instance.
     *
     * @param data                  - the data for creating.
     * @param interactionDataModel  - the data model that stored interactions.
     * @return the {@link RenderFrameStageCommand} instance.
     */
    RenderFrameStageCommand createRenderFrameStageCommand(FrameStageDataModel data, InteractionDataModel interactionDataModel);

    /**
     * Creates the {@link RenderFrameStageFromPreservationCommand} instance.
     */
    RenderFrameStageFromPreservationCommand createRenderFrameStageFromPreservationCommand();

    /**
     * Creates the {@link RenderFrameStageByParametersCommand} instance.
     *
     * @param mapName - the name of map that will be rendered.
     * @param skinName - the name of skin that will be used for acting character.
     * @param startPosition - the start position of acting character.
     * @return the {@link RenderFrameStageByParametersCommand} instance.
     */
    RenderFrameStageByParametersCommand createRenderFrameStageByParametersCommand(@Assisted("mapName") String mapName,
                                                                                  @Assisted("skinName") String skinName,
                                                                                  Point startPosition);

}
