package com.alta.dao.domain.quest.matcher;

import com.alta.dao.domain.quest.QuestListDeserializer;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.List;

/**
 * Provides the matcher for retrieve quests by name.
 */
public class QuestListItemNameMatcher implements QuestMatcher {

    private final List<String> names;

    /**
     * Initialize new instance of {@link QuestListItemNameMatcher}.
     *
     * @param names - the list of names that should be retrieved from list.
     */
    public QuestListItemNameMatcher(List<String> names) {
        this.names = names;
    }

    /**
     * Initialize new instance of {@link QuestListItemNameMatcher}.
     *
     * @param name - the name that should be retrieved from list.
     */
    public QuestListItemNameMatcher(String name) {
        this.names = Collections.singletonList(name);
    }

    /**
     * Indicates when json object is matched to be selected from list.
     *
     * @param jsonObject - the source json object.
     * @return true is json object should be retrieved, false otherwise.
     */
    @Override
    public boolean isMatched(JsonObject jsonObject) {
        if (!jsonObject.has(QuestListDeserializer.NAME_FIELD_NAME)) {
            return false;
        }

        String questName = jsonObject.get(QuestListDeserializer.NAME_FIELD_NAME).getAsString();
        if (Strings.isNullOrEmpty(questName)) {
            return false;
        }

        return this.names.stream().anyMatch(item -> item.equals(questName));
    }
}
