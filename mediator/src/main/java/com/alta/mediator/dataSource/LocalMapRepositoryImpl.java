package com.alta.mediator.dataSource;

import com.alta.behaviorprocess.data.localMap.LocalMapRepository;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.frameStage.FrameStageCommandFactory;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;

/**
 * Provides the repository that can make actions on a local map.
 */
@Slf4j
public class LocalMapRepositoryImpl implements LocalMapRepository {

    private final FrameStageCommandFactory frameStageCommandFactory;
    private final PreservationService preservationService;
    private final Long currentPreservationId;
    private final CommandExecutor commandExecutor;

    /**
     * Initialize new instance of {@link LocalMapRepositoryImpl}
     *
     * @param frameStageCommandFactory      - the {@link FrameStageCommandFactory} instance.
     * @param preservationService           - the {@link PreservationService} instance.
     * @param commandExecutor               - the {@link CommandExecutor} instance.
     * @param currentPreservationId         - the Id of current preservation.
     */
    @Inject
    public LocalMapRepositoryImpl(FrameStageCommandFactory frameStageCommandFactory,
                                  PreservationService preservationService, CommandExecutor commandExecutor,
                                  @Named("currentPreservationId") Long currentPreservationId) {
        this.frameStageCommandFactory = frameStageCommandFactory;
        this.preservationService = preservationService;
        this.commandExecutor = commandExecutor;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeJumping(String mapName, Point mapStartCoordinate) {
        String skinName = this.preservationService.getPreservation(this.currentPreservationId.intValue())
                .getActingCharacter()
                .getSkin();

        this.commandExecutor.executeCommand(
                this.frameStageCommandFactory.createRenderFrameStageByParametersCommand(
                        mapName,
                        skinName,
                        mapStartCoordinate
                )
        );
    }
}
