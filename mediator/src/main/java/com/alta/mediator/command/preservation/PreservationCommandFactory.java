package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.*;

/**
 * Provides the factory for commands related to preservation
 */
public interface PreservationCommandFactory {

    /**
     * Creates the command to update base part of preservation.
     *
     * @param globalPreservationModel - the preservation model to be used for updating.
     * @return created {@link UpdateGlobalPreservationCommand} instance.
     */
    UpdateGlobalPreservationCommand createUpdatePreservationCommand(GlobalPreservationModel globalPreservationModel);

    /**
     * Creates the command to update preservation of interaction.
     *
     * @param interactionPreservation - the preservation to be updated.
     * @return the {@link UpdateInteractionPreservationCommand} instance.
     */
    UpdateInteractionPreservationCommand createUpdateInteractionPreservationCommand(InteractionPreservationModel interactionPreservation);

    /**
     * Creates the command to update preservation of quest.
     *
     * @param questPreservationModel - the preservation to be updated.
     * @return the {@link UpdateQuestPreservationCommand} instance.
     */
    UpdateQuestPreservationCommand createUpdateQuestPreservationCommand(QuestPreservationModel questPreservationModel);

    /**
     * Creates the command to clear all temporary model related to preservation.
     *
     * @return the {@link ClearTemporaryPreservationDataCommand} instance.
     */
    ClearTemporaryPreservationDataCommand createClearTemporaryPreservationDataCommand();

    /**
     * Creates the save preservation command.
     *
     * @param model - the character preservation model to be saved.
     * @return the {@link SavePreservationCommand} instance.
     */
    SavePreservationCommand createSavePreservationCommand(CharacterPreservationModel model);

    /**
     * Create the update map preservation command.
     *
     * @param mapPreservationModel - the map preservation to be updated.
     * @return created {@link UpdateMapPreservationCommand} instance.
     */
    UpdateMapPreservationCommand createUpdateMapPreservationCommand(MapPreservationModel mapPreservationModel);
}
