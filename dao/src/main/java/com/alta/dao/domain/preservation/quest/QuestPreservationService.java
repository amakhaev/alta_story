package com.alta.dao.domain.preservation.quest;

import com.alta.dao.data.preservation.QuestPreservationModel;

/**
 * Provides the service to make CRUD operation with quest preservation
 */
public interface QuestPreservationService {

    /**
     * Gets the quest by given by given preservation id and uuid of quest uuid.
     *
     * @param preservationId    - the id of preservation.
     * @param name              - the name of quest to be retrieved.
     * @return found {@link QuestPreservationModel} instance.
     */
    QuestPreservationModel getQuestPreservation(Long preservationId, String name);

    /**
     * Gets the temporary quest by given by given preservation id and uuid of quest uuid.
     *
     * @param preservationId    - the id of preservation.
     * @param name              - the name of quest to be retrieved.
     * @return found {@link QuestPreservationModel} instance.
     */
    QuestPreservationModel getTemporaryQuestPreservation(Long preservationId, String name);

    /**
     * Clears the temporary model related to given preservation.
     *
     * @param preservationId - the id of preservation for which temporary model should be deleted.
     */
    void clearTemporaryData(Long preservationId);
}
