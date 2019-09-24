package com.alta.mediator.command.preservation;

import com.alta.dao.data.preservation.udt.ActingCharacterUdt;
import com.google.inject.assistedinject.Assisted;

/**
 * Provides the factory for commands related to preservation
 */
public interface PreservationCommandFactory {

    /**
     * Creates the command to update base part of preservation.
     *
     * @param chapterIndicator      - the new value of chapter indicator.
     * @param currentPreservationId - the current preservation id.
     * @return created {@link UpdateChapterIndicatorCommand} instance.
     */
    UpdateChapterIndicatorCommand createUpdatePreservationCommand(int chapterIndicator, Long currentPreservationId);

    /**
     * Creates the command to update preservation of interaction.
     *
     * @param preservationId    - the preservation ID.
     * @param interactionUuid   - the UUID of interaction.
     * @param mapName           - the name of map.
     * @param isComplete        - the complete status.
     * @return the {@link UpdateInteractionPreservationCommand} instance.
     */
    UpdateInteractionPreservationCommand createUpdateInteractionPreservationCommand(
            int preservationId,
            @Assisted("interactionUuid") String interactionUuid,
            @Assisted("mapName") String mapName,
            boolean isComplete
    );

    /**
     * Creates the command to update preservation of quest.
     *
     * @param preservationId    - the preservation ID.
     * @param name              - the name of quest.
     * @param currentStep       - the current step number.
     * @param isComplete        - the complete status.
     * @return the {@link UpdateQuestPreservationCommand} instance.
     */
    UpdateQuestPreservationCommand createUpdateQuestPreservationCommand(@Assisted("preservationId") int preservationId,
                                                                        String name,
                                                                        @Assisted("currentStep") int currentStep,
                                                                        boolean isComplete);

    /**
     * Creates the update map preservation command.
     *
     * @param preservationId    - the preservation ID.
     * @param participantUuid   - the UUID of participant.
     * @param mapName           - the map name.
     * @param isVisible         - the visible status.
     * @return created {@link UpdateMapPreservationCommand} instance.
     */
    UpdateMapPreservationCommand createUpdateMapPreservationCommand(int preservationId,
                                                                    @Assisted("participantUuid") String participantUuid,
                                                                    @Assisted("mapName") String mapName,
                                                                    boolean isVisible);

    /**
     * Creates the {@link UpdateActingCharacterCommand} instance.
     *
     * @param preservationId    - the preservation ID.
     * @param actingCharacter   - the acting character to be updated.
     * @return created {@link UpdateActingCharacterCommand} instance.
     */
    UpdateActingCharacterCommand createUpdateActingCharacterCommand(int preservationId, ActingCharacterUdt actingCharacter);

    /**
     * Creates the command to make snapshot.
     *
     * @param preservationId - the preservation ID.
     * @return created {@link MakeSnapshotCommand} instance.
     */
    MakeSnapshotCommand createMakeSnapshotCommand(int preservationId);

    /**
     * Creates the command to restore from snapshot.
     *
     * @param preservationId - the preservation ID.
     * @return created {@link RestoreSnapshotCommand} instance.
     */
    RestoreSnapshotCommand createRestoreSnapshotCommand(int preservationId);

}
