package com.alta.mediator.command.frameStage;

import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.alta.mediator.domain.frameStage.FrameStageDataProvider;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

/**
 * Provides the commands that renders the frame stage from preservation.
 */
@Slf4j
public class RenderFrameStageFromPreservationCommand implements Command {

    private final FrameStageDataProvider frameStageDataProvider;
    private final PreservationService preservationService;
    private final FrameStageCommandFactory frameStageCommandFactory;
    private final Long currentPreservationId;

    /**
     * Initialize ew instance of {@link RenderFrameStageFromPreservationCommand}.
     */
    @Inject
    public RenderFrameStageFromPreservationCommand(FrameStageDataProvider frameStageDataProvider,
                                                   PreservationService preservationService,
                                                   FrameStageCommandFactory frameStageCommandFactory,
                                                   @Named("currentPreservationId") Long currentPreservationId) {
        this.frameStageDataProvider = frameStageDataProvider;
        this.preservationService = preservationService;
        this.frameStageCommandFactory = frameStageCommandFactory;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        PreservationModel preservationModel = this.preservationService.getPreservation(this.currentPreservationId.intValue());
        if (preservationModel == null || preservationModel.getActingCharacter() == null) {
            log.error("Preservation model with given Id {} not found.", this.currentPreservationId);
            throw new NullPointerException("Preservation model with given Id not found.");
        }

        Command command = this.frameStageCommandFactory.createRenderFrameStageCommand(
                this.frameStageDataProvider.getFromPreservation(preservationModel)
        );
        command.execute();
    }
}
