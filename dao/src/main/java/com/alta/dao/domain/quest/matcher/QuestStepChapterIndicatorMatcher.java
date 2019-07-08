package com.alta.dao.domain.quest.matcher;

import com.alta.dao.domain.quest.QuestDeserializer;
import com.google.gson.JsonObject;

/**
 * Provides the matcher that get step by character indicators.
 */
public class QuestStepChapterIndicatorMatcher implements QuestMatcher {

    private final int chapterIndicatorFrom;
    private final int chapterIndicatorTo;

    /**
     * Initialize new instance of {@link QuestStepChapterIndicatorMatcher}.
     *
     * @param chapterIndicatorFrom  - the indicator of character from which step should be start.
     * @param chapterIndicatorTo    - the indicator of character to which step should be finished.
     */
    public QuestStepChapterIndicatorMatcher(int chapterIndicatorFrom, int chapterIndicatorTo) {
        this.chapterIndicatorFrom = chapterIndicatorFrom;
        this.chapterIndicatorTo = chapterIndicatorTo;
    }

    /**
     * Indicates when json object is matched to be selected from list.
     *
     * @param jsonObject - the source json object.
     * @return true is json object should be retrieved, false otherwise.
     */
    @Override
    public boolean isMatched(JsonObject jsonObject) {
        return jsonObject.has(QuestDeserializer.CHAPTER_INDICATOR_FROM_FIELD_NAME) &&
                jsonObject.get(QuestDeserializer.CHAPTER_INDICATOR_FROM_FIELD_NAME).getAsInt() >= this.chapterIndicatorFrom &&
                jsonObject.has(QuestDeserializer.CHAPTER_INDICATOR_TO_FIELD_NAME) &&
                jsonObject.get(QuestDeserializer.CHAPTER_INDICATOR_TO_FIELD_NAME).getAsInt() <= this.chapterIndicatorTo;
    }
}
