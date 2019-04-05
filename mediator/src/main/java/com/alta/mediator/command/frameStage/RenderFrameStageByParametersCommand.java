package com.alta.mediator.command.frameStage;

import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandFactory;
import com.alta.mediator.domain.frameStage.FrameStageDataProvider;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import java.awt.*;

/**
 * Provides the command to render frame stage by given parameters.
 */
public class RenderFrameStageByParametersCommand implements Command {

    private final FrameStageDataProvider frameStageDataProvider;
    private final CommandFactory commandFactory;
    private final String mapName;
    private final String skinName;
    private final Point startPosition;

    /**
     * Initialize new instance of {@link RenderFrameStageFromPreservationCommand}.
     */
    @AssistedInject
    public RenderFrameStageByParametersCommand(FrameStageDataProvider frameStageDataProvider,
                                               CommandFactory commandFactory,
                                               @Assisted("mapName") String mapName,
                                               @Assisted("skinName") String skinName,
                                               @Assisted Point startPosition) {
        this.frameStageDataProvider = frameStageDataProvider;
        this.commandFactory = commandFactory;
        this.mapName = mapName;
        this.skinName = skinName;
        this.startPosition = startPosition;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        Command command = this.commandFactory.createRenderFrameStageCommand(
                this.frameStageDataProvider.getByParams(this.mapName, this.skinName, this.startPosition)
        );
        command.execute();
    }
}
