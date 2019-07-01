package com.alta.dao.domain.quest.matcher;

import com.google.gson.JsonObject;

/**
 * Provides the matcher for search of list items.
 */
public interface QuestMatcher {

    /**
     * Indicates when json object is matched to be selected from list.
     *
     * @param jsonObject - the source json object.
     * @return true is json object should be retrieved, false otherwise.
     */
    boolean isMatched(JsonObject jsonObject);

}
