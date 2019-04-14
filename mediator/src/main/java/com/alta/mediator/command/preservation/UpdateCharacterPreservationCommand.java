package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Provides the command that updates the preservation related to character.
 */
public class UpdateCharacterPreservationCommand implements Command {

    private final PreservationService preservationService;
    private final CharacterPreservationModel characterPreservationModel;

    /**
     * Initialize new instance of {@link UpdateCharacterPreservationCommand}.
     */
    @AssistedInject
    public UpdateCharacterPreservationCommand(PreservationService preservationService,
                                              @Assisted CharacterPreservationModel characterPreservationModel) {
        this.preservationService = preservationService;
        this.characterPreservationModel = characterPreservationModel;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.preservationService.updateCharacterPreservation(this.characterPreservationModel);
    }
}
