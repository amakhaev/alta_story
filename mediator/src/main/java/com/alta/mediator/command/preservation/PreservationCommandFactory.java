package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;

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
