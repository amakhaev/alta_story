package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.udt.ActingCharacterUdt;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Provides the command to update acting character state.
 */
public class UpdateActingCharacterCommand implements Command {

    private final int preservationId;
    private final ActingCharacterUdt actingCharacter;
    private final PreservationService preservationService;

    /**
     * Initialize new instance of {@link UpdateActingCharacterCommand}.
     */
    @AssistedInject
    public UpdateActingCharacterCommand(@Assisted int preservationId,
                                        @Assisted ActingCharacterUdt actingCharacter,
                                        PreservationService preservationService) {
        this.preservationId = preservationId;
        this.actingCharacter = actingCharacter;
        this.preservationService = preservationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        this.preservationService.updateActingCharacter(this.preservationId, this.actingCharacter);
    }
}
