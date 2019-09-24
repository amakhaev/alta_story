package com.alta.mediator.command.preservation;

import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class UpdateQuestPreservationCommand implements Command {

    private final PreservationService preservationService;
    private final int preservationId;
    private final String name;
    private final int currentStep;
    private final boolean isComplete;

    /**
     * Initialize new instance of {@link UpdateQuestPreservationCommand}.
     */
    @AssistedInject
    public UpdateQuestPreservationCommand(PreservationService preservationService,
                                          @Assisted("preservationId") int preservationId,
                                          @Assisted String name,
                                          @Assisted("currentStep") int currentStep,
                                          @Assisted boolean isComplete) {
        this.preservationService = preservationService;
        this.preservationId = preservationId;
        this.name = name;
        this.currentStep = currentStep;
        this.isComplete = isComplete;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.preservationService.upsertQuest(this.preservationId, this.name, this.currentStep, this.isComplete);
    }
}
