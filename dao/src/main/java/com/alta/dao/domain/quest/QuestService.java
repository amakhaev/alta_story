package com.alta.dao.domain.quest;

import com.alta.dao.data.quest.QuestModel;

/**
 * Provides the service to make CRUD operations with quests.
 */
public interface QuestService {

    /**
     * Gets the quest by given path;
     *
     * @param path - the path to descriptor file/
     * @return found {@link QuestModel} instance.
     */
    QuestModel getQuest(String path);

}
