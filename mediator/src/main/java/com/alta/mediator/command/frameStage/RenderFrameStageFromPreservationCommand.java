package com.alta.mediator.command.frameStage;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.interaction.InteractionPreservationService;
import com.alta.dao.domain.quest.QuestListService;
import com.alta.dao.domain.quest.QuestService;
import com.alta.mediator.command.Command;
import com.alta.mediator.domain.frameStage.FrameStageDataProvider;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.List;

/**
 * Provides the commands that renders the frame stage from preservation.
 */
@Slf4j
public class RenderFrameStageFromPreservationCommand implements Command {

    private final FrameStageDataProvider frameStageDataProvider;
    private final PreservationService preservationService;
    private final InteractionPreservationService interactionPreservationService;
    private final FrameStageCommandFactory frameStageCommandFactory;
    private final Long currentPreservationId;
    private final QuestListService questListService;
    private final QuestService questService;

    /**
     * Initialize ew instance of {@link RenderFrameStageFromPreservationCommand}.
     */
    @Inject
    public RenderFrameStageFromPreservationCommand(FrameStageDataProvider frameStageDataProvider,
                                                   PreservationService preservationService,
                                                   InteractionPreservationService interactionPreservationService,
                                                   FrameStageCommandFactory frameStageCommandFactory,
                                                   @Named("currentPreservationId") Long currentPreservationId,
                                                   QuestListService questListService,
                                                   QuestService questService) {
        this.frameStageDataProvider = frameStageDataProvider;
        this.preservationService = preservationService;
        this.interactionPreservationService = interactionPreservationService;
        this.frameStageCommandFactory = frameStageCommandFactory;
        this.currentPreservationId = currentPreservationId;
        this.questListService = questListService;
        this.questService = questService;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        PreservationModel preservationModel = this.preservationService.getPreservation(this.currentPreservationId);
        if (preservationModel == null || preservationModel.getCharacterPreservation() == null) {
            log.error("Preservation model with given Id {} not found.", this.currentPreservationId);
            throw new NullPointerException("Preservation model with given Id not found.");
        }

        // Add saved interactions.
        List<InteractionPreservationModel> interactionPreservations = this.interactionPreservationService.getInteractionsPreservation(
                this.currentPreservationId,
                preservationModel.getCharacterPreservation().getMapName()
        );

        // Add temporary saved interactions.
        interactionPreservations.addAll(
                this.interactionPreservationService.getTemporaryInteractionsPreservation(
                        this.currentPreservationId,
                        preservationModel.getCharacterPreservation().getMapName()
                )
        );

        Command command = this.frameStageCommandFactory.createRenderFrameStageCommand(
                this.frameStageDataProvider.getFromPreservation(preservationModel.getCharacterPreservation())
        );
        command.execute();
    }
}
