package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.character.CharacterPreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;

/**
 * Provides the command to save the current state to preservation.
 */
public class SavePreservationCommand implements Command {

    private final CharacterPreservationService characterPreservationService;
    private final CharacterPreservationModel characterPreservationModel;
    private final PreservationService preservationService;

    /**
     * Initialize new instance of {@link SavePreservationCommand}.
     */
    @AssistedInject
    public SavePreservationCommand(CharacterPreservationService characterPreservationService,
                                   PreservationService preservationService,
                                   @NonNull @Assisted CharacterPreservationModel characterPreservationModel) {
        this.preservationService = preservationService;
        this.characterPreservationService = characterPreservationService;
        this.characterPreservationModel = characterPreservationModel;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.preservationService.markTemporaryAsCompletelySaved(
                this.characterPreservationModel.getId() // The same id for preservation
        );
        this.characterPreservationService.updateCharacterPreservation(this.characterPreservationModel);
    }
}
