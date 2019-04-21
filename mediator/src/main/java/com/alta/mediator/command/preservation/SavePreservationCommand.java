package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.TemporaryDataPreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;

/**
 * Provides the command to save the current state to preservation.
 */
public class SavePreservationCommand implements Command {

    private final PreservationService preservationService;
    private final TemporaryDataPreservationService temporaryDataPreservationService;
    private final CharacterPreservationModel characterPreservationModel;

    /**
     * Initialize new instance of {@link SavePreservationCommand}.
     */
    @AssistedInject
    public SavePreservationCommand(PreservationService preservationService,
                                   TemporaryDataPreservationService temporaryDataPreservationService,
                                   @NonNull @Assisted CharacterPreservationModel characterPreservationModel) {
        this.preservationService = preservationService;
        this.temporaryDataPreservationService = temporaryDataPreservationService;
        this.characterPreservationModel = characterPreservationModel;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.temporaryDataPreservationService.markTemporaryInteractionsAsCompletelySaved(
                this.characterPreservationModel.getId() // The same id for preservation
        );
        this.preservationService.updateCharacterPreservation(this.characterPreservationModel);
    }
}
