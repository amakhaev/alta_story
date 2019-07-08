package com.alta.mediator.dataSource;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.engine.data.EngineRepository;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.frameStage.FrameStageCommandFactory;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;

/**
 * Provides the repository to make CRUD with model related to engine in general.
 */
@Slf4j
public class EngineRepositoryImpl implements EngineRepository {

    private final FrameStageCommandFactory frameStageCommandFactory;
    private final PreservationCommandFactory preservationCommandFactory;
    private final PreservationService preservationService;
    private final Long currentPreservationId;
    private final CommandExecutor commandExecutor;

    /**
     * Initialize new instance of {@link EngineRepositoryImpl}
     * @param frameStageCommandFactory      - the {@link FrameStageCommandFactory} instance.
     * @param preservationCommandFactory    - the {@link PreservationCommandFactory} instance.
     * @param preservationService           - the {@link PreservationService} instance.
     * @param commandExecutor               - the {@link CommandExecutor} instance.
     * @param currentPreservationId         - the Id of current preservation.
     */
    @Inject
    public EngineRepositoryImpl(FrameStageCommandFactory frameStageCommandFactory,
                                PreservationCommandFactory preservationCommandFactory,
                                PreservationService preservationService, CommandExecutor commandExecutor,
                                @Named("currentPreservationId") Long currentPreservationId) {
        this.frameStageCommandFactory = frameStageCommandFactory;
        this.preservationCommandFactory = preservationCommandFactory;
        this.preservationService = preservationService;
        this.commandExecutor = commandExecutor;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Saves the state of game.
     *
     * @param mapName                      - the name of map where acting character stay currently.
     * @param actionCharacterSkin          - the skin name of acting character.
     * @param actionCharacterMapCoordinate - the coordinates on map of acting character.
     */
    @Override
    public void saveState(@NonNull String mapName,
                          @NonNull String actionCharacterSkin,
                          @NonNull Point actionCharacterMapCoordinate) {
        CharacterPreservationModel characterPreservationModel = CharacterPreservationModel.builder()
                .id(this.currentPreservationId)
                .focusX(actionCharacterMapCoordinate.x)
                .focusY(actionCharacterMapCoordinate.y)
                .skin(actionCharacterSkin)
                .mapName(mapName)
                .build();

        Command command = this.preservationCommandFactory.createSavePreservationCommand(characterPreservationModel);
        this.commandExecutor.executeCommand(command);
        log.info("Saving of preservation completed to preservation {}", this.currentPreservationId);
    }

    /**
     * Makes the jump to another map.
     *
     * @param mapName            - the name of map where acting character stay currently.
     * @param mapStartCoordinate - the coordinates on map of acting character.
     */
    @Override
    public void makeJumping(String mapName, Point mapStartCoordinate) {
        String skinName = this.preservationService.getPreservation(this.currentPreservationId)
                .getCharacterPreservation().getMainCharaterSkin();

        this.commandExecutor.executeCommand(
                this.frameStageCommandFactory.createRenderFrameStageByParametersCommand(
                        mapName,
                        skinName,
                        mapStartCoordinate
                )
        );
    }
}
