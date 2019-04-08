package com.alta.mediator.command.frameStage;

import com.alta.dao.domain.characterPreservation.CharacterPreservationService;
import com.alta.mediator.command.Command;
import com.alta.mediator.domain.frameStage.FrameStageDataProvider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the commands that renders the frame stage from preservation.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RenderFrameStageFromPreservationCommand implements Command {

    private final FrameStageDataProvider frameStageDataProvider;
    private final CharacterPreservationService characterPreservationService;
    private final FrameStageCommandFactory frameStageCommandFactory;

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        Command command = this.frameStageCommandFactory.createRenderFrameStageCommand(
                this.frameStageDataProvider.getFromPreservation(
                        this.characterPreservationService.getCharacterPreservation(1L)
                )
        );
        command.execute();
    }
}
