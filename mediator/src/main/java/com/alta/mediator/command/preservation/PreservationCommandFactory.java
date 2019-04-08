package com.alta.mediator.command.preservation;

import com.alta.dao.data.characterPreservation.CharacterPreservationModel;

/**
 * Provides the factory for commands related to preservation
 */
public interface PreservationCommandFactory {

    /**
     * Creates the command to update preservation of character.
     *
     * @param model - the model to be used for update.
     * @return the {@link UpdateCharacterPreservationCommand} instance.
     */
    UpdateCharacterPreservationCommand createUpdateCharacterPreservationCommand(CharacterPreservationModel model);

}
