package com.alta.mediator.dataSource;

import com.alta.behaviorprocess.data.interaction.InteractionRepository;
import com.alta.behaviorprocess.data.interaction.InteractionModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.interaction.InteractionPreservationService;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.interaction.InteractionCommandFactory;
import com.alta.mediator.domain.interaction.InteractionDataProvider;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Provides the implementation of {@link InteractionRepository}.
 */
@Slf4j
public class InteractionRepositoryImpl implements InteractionRepository {

    private final PreservationService preservationService;
    private final InteractionPreservationService interactionPreservationService;
    private final InteractionDataProvider interactionDataProvider;
    private final InteractionCommandFactory interactionCommandFactory;
    private final CommandExecutor commandExecutor;
    private final Long currentPreservationId;

    /**
     * Initialize new instance of {@link InteractionRepositoryImpl}.
     * @param preservationService               - the {@link PreservationService} instance.
     * @param interactionPreservationService    - the {@link InteractionPreservationService} instance.
     * @param interactionDataProvider           - the {@link InteractionDataProvider} instance.
     * @param interactionCommandFactory         - the {@link InteractionCommandFactory} instance.
     * @param commandExecutor                   - the {@link CommandExecutor} instance.
     * @param currentPreservationId             - the Id of current preservation.
     */
    @Inject
    public InteractionRepositoryImpl(PreservationService preservationService,
                                     InteractionPreservationService interactionPreservationService,
                                     InteractionDataProvider interactionDataProvider,
                                     InteractionCommandFactory interactionCommandFactory,
                                     CommandExecutor commandExecutor,
                                     @Named("currentPreservationId") Long currentPreservationId) {
        this.preservationService = preservationService;
        this.interactionPreservationService = interactionPreservationService;
        this.interactionDataProvider = interactionDataProvider;
        this.interactionCommandFactory = interactionCommandFactory;
        this.commandExecutor = commandExecutor;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Finds the interactions for target map.
     *
     * @param mapName - the name of map where interaction available.
     * @return found {@link List<InteractionModel>} instance.
     */
    @Override
    public List<InteractionModel> findInteractions(String mapName) {
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

        return this.interactionDataProvider.getInteractionsByRelatedMapName(
                mapName, preservationModel.getGlobalPreservation().getChapterIndicator(), interactionPreservations
        );
    }

    /**
     * Completes the interaction.
     *
     * @param interactionUuid - the uuid of interaction that was completed.
     * @param mapName         - the name of map related to interaction.
     */
    @Override
    public void completeInteraction(@NonNull String interactionUuid, @NonNull String mapName) {
        this.commandExecutor.executeCommand(
                this.interactionCommandFactory.createCompleteInteractionCommand(interactionUuid, mapName)
        );
    }


}
