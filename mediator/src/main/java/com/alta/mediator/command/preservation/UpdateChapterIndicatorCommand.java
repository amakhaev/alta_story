package com.alta.mediator.command.preservation;

import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.Command;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import lombok.extern.slf4j.Slf4j;

/**
 * Update the chapter indicator in the preservation.
 */
@Slf4j
public class UpdateChapterIndicatorCommand implements Command {

    private final int chapterIndicator;
    private final PreservationService preservationService;
    private final Long preservationId;

    /**
     * Initialize new instance of {@link UpdateChapterIndicatorCommand}.
     * @param preservationService   - the {@link PreservationService} instance.
     * @param chapterIndicator      - the new value of chapter indicator.
     * @param preservationId        - the current preservation id.
     */
    @AssistedInject
    public UpdateChapterIndicatorCommand(PreservationService preservationService,
                                         @Assisted int chapterIndicator,
                                         @Assisted Long preservationId) {
        this.preservationService = preservationService;
        this.chapterIndicator = chapterIndicator;
        this.preservationId = preservationId;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        this.preservationService.updateChapterIndicator(this.preservationId.intValue(), this.chapterIndicator);
    }
}
