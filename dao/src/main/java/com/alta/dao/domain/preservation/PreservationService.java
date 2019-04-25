package com.alta.dao.domain.preservation;

import com.alta.dao.data.interaction.InteractionModel;
import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;

import java.util.List;

/**
 * Provides the service to make CRUD operation with preservation
 */
public interface PreservationService {

    /**
     * Gets the preservation related of game.
     *
     * @param id - the identifier preservation of character.
     * @return the {@link CharacterPreservationModel} instance.
     */
    PreservationModel getPreservation(Long id);

    /**
     * Updates the preservation that related to character.
     *
     * @param characterPreservationModel - the model to be updated.
     */
    void updateCharacterPreservation(CharacterPreservationModel characterPreservationModel);

    /**
     * Clears all temporary data that related to specific preservation.
     *
     * @param preservationId - the preservation to be cleared.
     */
    void clearTemporaryDataFromPreservation(Long preservationId);

    /**
     * Gets the list of interactions that related to preservation.
     *
     * @param preservationId    - the preservation id.
     * @param mapName           - the name of map.
     * @return the {@link List} of {@link InteractionPreservationModel} related to specific map and preservation.
     */
    List<InteractionPreservationModel> getInteractionsPreservation(Long preservationId, String mapName);

    /**
     * Finds the saved interaction by given preservation id and uuid of interaction.
     *
     * @param preservationId    - the preservation id.
     * @param interactionUuid   - the interaction uuid.
     * @return the {@link InteractionPreservationModel} instance or null if not found.
     */
    InteractionPreservationModel findInteractionByPreservationIdAndUuid(Long preservationId, String interactionUuid);
}
