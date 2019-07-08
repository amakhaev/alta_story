package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.domain.preservation.character.CharacterPreservationService;
import com.alta.dao.domain.preservation.interaction.InteractionPreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.NonNull;

/**
 * Provides the command to save the current state to preservation.
 */
public class SavePreservationCommand implements Command {

    private final InteractionPreservationService interactionPreservationService;
    private final CharacterPreservationService characterPreservationService;
    private final CharacterPreservationModel characterPreservationModel;

    /**
     * Initialize new instance of {@link SavePreservationCommand}.
     */
    @AssistedInject
    public SavePreservationCommand(InteractionPreservationService interactionPreservationService,
                                   CharacterPreservationService characterPreservationService,
                                   @NonNull @Assisted CharacterPreservationModel characterPreservationModel) {
        this.interactionPreservationService = interactionPreservationService;
        this.characterPreservationService = characterPreservationService;
        this.characterPreservationModel = characterPreservationModel;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.interactionPreservationService.markTemporaryInteractionsAsCompletelySaved(
                this.characterPreservationModel.getId() // The same id for preservation
        );
        this.characterPreservationService.updateCharacterPreservation(this.characterPreservationModel);
    }
}
