package com.alta.mediator.domain.effect;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.common.effect.background.UpdateChapterIndicatorDataModel;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Provides the service for working with background post effects.
 */
@Slf4j
public class BackgroundEffectServiceImpl implements BackgroundEffectService {

    private final CommandExecutor commandExecutor;
    private final PreservationCommandFactory preservationCommandFactory;
    private final Long currentPreservationId;

    /**
     * Initialize new instance of {@link BackgroundEffectServiceImpl}.
     * @param commandExecutor               - the {@link CommandExecutor} instance.
     * @param preservationCommandFactory    - the {@link PreservationCommandFactory} instance.
     * @param currentPreservationId         - the current preservation id.
     */
    @Inject
    public BackgroundEffectServiceImpl(CommandExecutor commandExecutor,
                                       PreservationCommandFactory preservationCommandFactory,
                                       @Named("currentPreservationId") Long currentPreservationId) {
        this.commandExecutor = commandExecutor;
        this.preservationCommandFactory = preservationCommandFactory;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Executes the background effects.
     *
     * @param effects - the list of effects to be executed.
     */
    @Override
    public void executeBackgroundEffects(List<EffectDataModel> effects) {
        if (effects == null || effects.isEmpty()) {
            return;
        }

        effects.forEach(effect -> {
            switch (effect.getType()) {
                case UPDATE_CHAPTER_INDICATOR:
                    this.updateChapterIndicator(((UpdateChapterIndicatorDataModel) effect).getChapterValue());
                    break;
                default:
                    log.warn("Unsupported type of effect: {}", effect.getType());
            }
        });
    }

    private void updateChapterIndicator(int chapterValue) {
        this.commandExecutor.executeCommand(
                this.preservationCommandFactory.createUpdatePreservationCommand(chapterValue, this.currentPreservationId)
        );
    }
}
