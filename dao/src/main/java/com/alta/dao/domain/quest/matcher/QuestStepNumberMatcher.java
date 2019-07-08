package com.alta.dao.domain.quest.matcher;

import com.alta.dao.domain.quest.QuestDeserializer;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;

/**
 * Provides the matcher that get step by current step.
 */
@RequiredArgsConstructor
public class QuestStepNumberMatcher implements QuestMatcher {

    private final int currentStepNumber;

    /**
     * Indicates when json object is matched to be selected from list.
     *
     * @param jsonObject - the source json object.
     * @return true is json object should be retrieved, false otherwise.
     */
    @Override
    public boolean isMatched(JsonObject jsonObject) {
        return jsonObject.has(QuestDeserializer.STEP_NUMBER_FIELD_NAME) &&
                jsonObject.get(QuestDeserializer.STEP_NUMBER_FIELD_NAME).getAsInt() == this.currentStepNumber;
    }
}
