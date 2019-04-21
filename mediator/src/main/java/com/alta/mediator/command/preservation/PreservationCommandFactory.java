package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;

/**
 * Provides the factory for commands related to preservation
 */
public interface PreservationCommandFactory {

    /**
     * Creates the command to update preservation of interaction.
     *
     * @param interactionPreservation - the preservation to be updated.
     * @return the {@link UpdateInteractionPreservationCommand} instance.
     */
    UpdateInteractionPreservationCommand createUpdateInteractionPreservationCommand(InteractionPreservationModel interactionPreservation);

    /**
     * Creates the command to clear all temporary data related to preservation.
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
}
