package com.alta.mediator.command.preservation;

import com.alta.dao.data.characterPreservation.CharacterPreservationModel;
import com.alta.dao.domain.characterPreservation.CharacterPreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Provides the command that updates the preservation related to character.
 */
public class UpdateCharacterPreservationCommand implements Command {

    private final CharacterPreservationService characterPreservationService;
    private final CharacterPreservationModel characterPreservationModel;

    /**
     * Initialize new instance of {@link UpdateCharacterPreservationCommand}.
     */
    @AssistedInject
    public UpdateCharacterPreservationCommand(CharacterPreservationService characterPreservationService,
                                              @Assisted CharacterPreservationModel characterPreservationModel) {
        this.characterPreservationService = characterPreservationService;
        this.characterPreservationModel = characterPreservationModel;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.characterPreservationService.updateCharacterPreservation(this.characterPreservationModel);
    }
}
