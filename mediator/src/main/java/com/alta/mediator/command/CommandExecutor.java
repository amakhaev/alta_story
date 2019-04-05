package com.alta.mediator.command;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the executor of commands
 */
@Slf4j
public class CommandExecutor {

    /**
     * Executes the command.
     *
     * @param command - the command to be executed.
     */
    public void executeCommand(@NonNull Command command) {
        log.debug("Start the execution of command {}", command.getClass().getSimpleName());
        command.execute();
        log.debug("Finish the execution of command {}", command.getClass().getSimpleName());
    }

}
