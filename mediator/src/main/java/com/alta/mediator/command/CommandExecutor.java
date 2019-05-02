package com.alta.mediator.command;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
    public synchronized void executeCommand(@NonNull Command command) {
        log.debug("Start the execution of command {}", command.getClass().getSimpleName());
        command.execute();
        log.debug("Finish the execution of command {}", command.getClass().getSimpleName());
    }

    /**
     * Executes the batch of commands.
     *
     * @param commands - the commands to be executed.
     */
    public synchronized void executeCommands(List<Command> commands) {
        log.debug("Have got {} command to be executed", commands.size());
        commands.forEach(this::executeCommand);
        log.debug("Execution of commands completed");
    }
}
