package com.alta.mediator.command.frameStage;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.interaction.InteractionPreservationService;
import com.alta.mediator.command.Command;
import com.alta.mediator.domain.frameStage.FrameStageDataProvider;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import javax.inject.Named;
import java.awt.*;
import java.util.List;

/**
 * Provides the command to render frame stage by given parameters.
 */
public class RenderFrameStageByParametersCommand implements Command {

    private final FrameStageDataProvider frameStageDataProvider;
    private final FrameStageCommandFactory frameStageCommandFactory;
    private final PreservationService preservationService;
    private final InteractionPreservationService interactionPreservationService;
    private final Long currentPreservationId;
    private final String mapName;
    private final String skinName;
    private final Point startPosition;

    /**
     * Initialize new instance of {@link RenderFrameStageFromPreservationCommand}.
     */
    @AssistedInject
    public RenderFrameStageByParametersCommand(FrameStageDataProvider frameStageDataProvider,
                                               FrameStageCommandFactory frameStageCommandFactory,
                                               PreservationService preservationService,
                                               InteractionPreservationService interactionPreservationService,
                                               @Named("currentPreservationId") Long currentPreservationId,
                                               @Assisted("mapName") String mapName,
                                               @Assisted("skinName") String skinName,
                                               @Assisted Point startPosition) {
        this.frameStageDataProvider = frameStageDataProvider;
        this.frameStageCommandFactory = frameStageCommandFactory;
        this.preservationService = preservationService;
        this.interactionPreservationService = interactionPreservationService;
        this.currentPreservationId = currentPreservationId;
        this.mapName = mapName;
        this.skinName = skinName;
        this.startPosition = startPosition;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        PreservationModel currentPreservation = this.preservationService.getPreservation(this.currentPreservationId);
        if (currentPreservation == null) {
            throw new NullPointerException("Current preservation not found");
        }

        // Add saved interactions.
        List<InteractionPreservationModel> interactionPreservations = this.interactionPreservationService.getInteractionsPreservation(
                this.currentPreservationId,
                this.mapName
        );

        // Add temporary saved interactions.
        interactionPreservations.addAll(
                this.interactionPreservationService.getTemporaryInteractionsPreservation(
                        this.currentPreservationId,
                        this.mapName
                )
        );

        Command command = this.frameStageCommandFactory.createRenderFrameStageCommand(
                this.frameStageDataProvider.getByParams(this.mapName, this.skinName, this.startPosition)
        );
        command.execute();
    }
}
