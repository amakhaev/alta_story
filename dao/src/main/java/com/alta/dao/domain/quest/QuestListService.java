package com.alta.dao.domain.quest;

import com.alta.dao.data.quest.QuestListItemModel;

/**
 * Provides the service to make CRUD operations with quest list.
 */
public interface QuestListService {

    /**
     * Finds the quest by given name.
     *
     * @param name - the name of quest.
     * @return found {@link QuestListItemModel} instance.
     */
    QuestListItemModel findQuestByName(String name);

}
