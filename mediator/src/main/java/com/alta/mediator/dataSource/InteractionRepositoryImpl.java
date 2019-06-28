package com.alta.mediator.dataSource;

import com.alta.behaviorprocess.behaviorAction.interaction.InteractionRepository;
import com.alta.behaviorprocess.shared.data.InteractionModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.TemporaryDataPreservationService;
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
    private final TemporaryDataPreservationService temporaryDataPreservationService;
    private final InteractionDataProvider interactionDataProvider;
    private final InteractionCommandFactory interactionCommandFactory;
    private final CommandExecutor commandExecutor;
    private final Long currentPreservationId;

    /**
     * Initialize new instance of {@link InteractionRepositoryImpl}.
     * @param preservationService               - the {@link PreservationService} instance.
     * @param temporaryDataPreservationService  - the {@link TemporaryDataPreservationService} instance.
     * @param interactionDataProvider           - the {@link InteractionDataProvider} instance.
     * @param interactionCommandFactory         - the {@link InteractionCommandFactory} instance.
     * @param commandExecutor                   - the {@link CommandExecutor} instance.
     * @param currentPreservationId             - the Id of current preservation.
     */
    @Inject
    public InteractionRepositoryImpl(PreservationService preservationService,
                                     TemporaryDataPreservationService temporaryDataPreservationService,
                                     InteractionDataProvider interactionDataProvider,
                                     InteractionCommandFactory interactionCommandFactory,
                                     CommandExecutor commandExecutor,
                                     @Named("currentPreservationId") Long currentPreservationId) {
        this.preservationService = preservationService;
        this.temporaryDataPreservationService = temporaryDataPreservationService;
        this.interactionDataProvider = interactionDataProvider;
        this.interactionCommandFactory = interactionCommandFactory;
        this.commandExecutor = commandExecutor;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Finds the interaction for target participant on map.
     *
     * @param mapName    - the name of map where interaction available.
     * @param targetUuid - the uuid of target for which interaction needed.
     * @return found {@link InteractionModel} instance or null.
     */
    @Override
    public InteractionModel findInteraction(@NonNull String mapName, @NonNull String targetUuid) {
        PreservationModel preservationModel = this.preservationService.getPreservation(this.currentPreservationId);
        if (preservationModel == null || preservationModel.getCharacterPreservation() == null) {
            log.error("Preservation data with given Id {} not found.", this.currentPreservationId);
            throw new NullPointerException("Preservation data with given Id not found.");
        }

        // Add saved interactions.
        List<InteractionPreservationModel> interactionPreservations = this.preservationService.getInteractionsPreservation(
                this.currentPreservationId,
                preservationModel.getCharacterPreservation().getMapName()
        );

        // Add temporary saved interactions.
        interactionPreservations.addAll(
                this.temporaryDataPreservationService.getTemporaryInteractionsPreservation(
                        this.currentPreservationId,
                        preservationModel.getCharacterPreservation().getMapName()
                )
        );

        return this.interactionDataProvider.getInteractionByRelatedMapName(
                mapName, targetUuid, preservationModel.getChapterIndicator(), interactionPreservations
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
