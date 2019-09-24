package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.data.preservation.QuestPreservationModel;
import com.alta.dao.data.preservation.udt.ActingCharacterUdt;

import java.util.List;
import java.util.UUID;

/**
 * Provides the service to make CRUD operation with preservation
 */
public interface PreservationService {

    /**
     * Updates the chapter indicator in the database.
     *
     * @param id                - the preservation id to be updated.
     * @param chapterIndicator  - the new value of chapter indicator.
     */
    void updateChapterIndicator(int id, int chapterIndicator);

    /**
     * Updates the acting character in the database.
     *
     * @param preservationId    - the preservation ID.
     * @param actingCharacter   - the acting character to be updated.
     */
    void updateActingCharacter(int preservationId, ActingCharacterUdt actingCharacter);

    /**
     * Updates or inserts the interaction into database.
     *
     * @param preservationId    - the preservation ID.
     * @param interactionUuid   - the UUID of interaction.
     * @param mapName           - the name of map.
     * @param isComplete        - the complete status.
     */
    void upsertInteraction(int preservationId, UUID interactionUuid, String mapName, boolean isComplete);

    /**
     * Updates or inserts the quest into database.
     *
     * @param preservationId    - the preservation ID.
     * @param name              - the name of quest.
     * @param currentStep       - the current step number.
     * @param isComplete        - the complete status.
     */
    void upsertQuest(int preservationId, String name, int currentStep, boolean isComplete);

    /**
     * Updates or inserts the map into database.
     *
     * @param preservationId    - the preservation ID.
     * @param participantUuid   - the UUID of participant.
     * @param mapName           - the map name.
     * @param isVisible         - the visible status.
     */
    void upsertMap(int preservationId, UUID participantUuid, String mapName, boolean isVisible);

    /**
     * Gets the quest by given name.
     *
     * @param preservationId    - the preservation ID.
     * @param questName         - the name of quest to be retrieved.
     * @return the {@link QuestPreservationModel} instance.
     */
    QuestPreservationModel getQuest(int preservationId, String questName);

    /**
     * Gets the preservation by given ID.
     *
     * @param preservationId - the preservation ID.
     * @return the {@link PreservationModel} instance.
     */
    PreservationModel getPreservation(int preservationId);

    /**
     * Gets the interaction by given name.
     *
     * @param preservationId    - the preservation ID.
     * @param interactionUuid   - the UUID of interaction.
     * @return the {@link InteractionPreservationModel} instance.
     */
    InteractionPreservationModel getInteraction(int preservationId, UUID interactionUuid);

    /**
     * Gets the list of available maps for given preservation.
     *
     * @param preservationId    - the preservation ID.
     * @param mapName           - the name of map.
     * @return the list of {@link List} of maps.
     */
    List<MapPreservationModel> getMaps(int preservationId, String mapName);

    /**
     * Gets the list of available interaction for given preservation.
     *
     * @param preservationId    - the preservation ID.
     * @param mapName           - the name of map.
     * @return the list of {@link List} of interactions.
     */
    List<InteractionPreservationModel> getInteractions(int preservationId, String mapName);
}
