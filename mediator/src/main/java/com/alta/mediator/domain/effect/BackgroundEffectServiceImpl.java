package com.alta.mediator.domain.effect;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.common.effect.background.UpdateChapterIndicatorDataModel;
import com.alta.dao.data.preservation.GlobalPreservationModel;
import com.alta.dao.domain.preservation.global.GlobalPreservationService;
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
    private final GlobalPreservationService globalPreservationService;
    private final Long currentPreservationId;

    /**
     * Initialize new instance of {@link BackgroundEffectServiceImpl}.
     * @param commandExecutor               - the {@link CommandExecutor} instance.
     * @param preservationCommandFactory    - the {@link PreservationCommandFactory} instance.
     * @param globalPreservationService     - the {@link GlobalPreservationService} instance.
     * @param currentPreservationId         - the current preservation id.
     */
    @Inject
    public BackgroundEffectServiceImpl(CommandExecutor commandExecutor,
                                       PreservationCommandFactory preservationCommandFactory,
                                       GlobalPreservationService globalPreservationService,
                                       @Named("currentPreservationId") Long currentPreservationId) {
        this.commandExecutor = commandExecutor;
        this.preservationCommandFactory = preservationCommandFactory;
        this.globalPreservationService = globalPreservationService;
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
        GlobalPreservationModel globalPreservation = this.globalPreservationService.getTemporaryGlobalPreservation(
                this.currentPreservationId
        );

        if (globalPreservation == null) {
            globalPreservation = this.globalPreservationService.getGlobalPreservation(this.currentPreservationId);

            if (globalPreservation == null) {
                log.error("The global preservation with id {} not found into storage", this.currentPreservationId);
                return;
            }

            globalPreservation.setId(null);
        }

        globalPreservation.setChapterIndicator(chapterValue);
        this.commandExecutor.executeCommand(
                this.preservationCommandFactory.createUpdatePreservationCommand(globalPreservation)
        );
    }
}
