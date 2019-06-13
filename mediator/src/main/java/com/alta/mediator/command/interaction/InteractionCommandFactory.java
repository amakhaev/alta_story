package com.alta.mediator.command.interaction;

import com.google.inject.assistedinject.Assisted;

/**
 * Provides the factory for commands related to interactions.
 */
public interface InteractionCommandFactory {

    /**
     * Creates the command that is completing interaction.
     *
     * @param interactionUuid   - the uuid of interaction that was completed.
     * @param relatedMapName    - the name of map where interaction was executing.
     * @return created {@link CompleteInteractionCommand} instance.
     */
    CompleteInteractionCommand createCompleteInteractionCommand(@Assisted("interactionUuid") String interactionUuid,
                                                                @Assisted("relatedMapName") String relatedMapName);

}
