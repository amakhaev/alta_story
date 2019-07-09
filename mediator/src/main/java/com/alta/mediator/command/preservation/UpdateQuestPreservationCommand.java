package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.domain.preservation.quest.QuestPreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class UpdateQuestPreservationCommand implements Command {

    private final QuestPreservationService questPreservationService;
    private final QuestPreservationModel questPreservationModel;

    /**
     * Initialize new instance of {@link UpdateQuestPreservationCommand}.
     *
     * @param questPreservationService  - the {@link QuestPreservationService} instance.
     * @param questPreservationModel    - the quest preservation to be update.
     */
    @AssistedInject
    public UpdateQuestPreservationCommand(QuestPreservationService questPreservationService,
                                          @Assisted QuestPreservationModel questPreservationModel) {
        this.questPreservationService = questPreservationService;
        this.questPreservationModel = questPreservationModel;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.questPreservationService.upsertTemporaryQuestPreservation(this.questPreservationModel);
    }
}
