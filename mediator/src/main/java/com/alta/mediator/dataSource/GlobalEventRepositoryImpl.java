package com.alta.mediator.dataSource;

import com.alta.behaviorprocess.data.globalEvent.GlobalEventRepository;
import com.alta.dao.data.preservation.udt.ActingCharacterUdt;
import com.alta.mediator.command.Command;
import com.alta.mediator.command.CommandExecutor;
import com.alta.mediator.command.preservation.PreservationCommandFactory;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Provides the repository tp handle global events.
 */
@Slf4j
public class GlobalEventRepositoryImpl implements GlobalEventRepository {

    private final PreservationCommandFactory preservationCommandFactory;
    private final Long currentPreservationId;
    private final CommandExecutor commandExecutor;

    /**
     * Initialize new instance of {@link GlobalEventRepositoryImpl}
     *
     * @param preservationCommandFactory    - the {@link PreservationCommandFactory} instance.
     * @param commandExecutor               - the {@link CommandExecutor} instance.
     * @param currentPreservationId         - the Id of current preservation.
     */
    @Inject
    public GlobalEventRepositoryImpl(PreservationCommandFactory preservationCommandFactory,
                                     CommandExecutor commandExecutor,
                                     @Named("currentPreservationId") Long currentPreservationId) {
        this.preservationCommandFactory = preservationCommandFactory;
        this.commandExecutor = commandExecutor;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveState(String mapName, String actionCharacterSkin, Point actionCharacterMapCoordinate) {
        ActingCharacterUdt characterUdt = ActingCharacterUdt.builder()
                .focusX(actionCharacterMapCoordinate.x)
                .focusY(actionCharacterMapCoordinate.y)
                .skin(actionCharacterSkin)
                .mapName(mapName)
                .build();

        List<Command> commands = Arrays.asList(
                this.preservationCommandFactory.createUpdateActingCharacterCommand(
                        this.currentPreservationId.intValue(), characterUdt
                ),
                this.preservationCommandFactory.createMakeSnapshotCommand(this.currentPreservationId.intValue())
        );
        this.commandExecutor.executeCommands(commands);


        log.info("Saving of preservation completed to preservation {}", this.currentPreservationId);
    }
}
