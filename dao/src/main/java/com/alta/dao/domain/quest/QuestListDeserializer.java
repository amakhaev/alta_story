package com.alta.dao.domain.quest;

import com.alta.dao.data.quest.QuestListItemModel;
import com.alta.dao.domain.quest.matcher.QuestListItemNameMatcher;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class QuestListDeserializer implements JsonDeserializer<List<QuestListItemModel>> {

    public static final String NAME_FIELD_NAME = "name";
    public static final String PATH_TO_DESCRIPTOR_FIELD_NAME = "pathToDescriptor";
    public static final String CHAPTER_INDICATOR_FROM_FIELD_NAME = "chapterIndicatorFrom";
    public static final String CHAPTER_INDICATOR_TO_FIELD_NAME = "chapterIndicatorTo";

    private final List<QuestListItemNameMatcher> matchers;

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json model being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public List<QuestListItemModel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonQuests = json.getAsJsonArray();

        List<QuestListItemModel> result = new ArrayList<>();
        jsonQuests.forEach(jsonQuestListItem -> {
            JsonObject item = jsonQuestListItem.getAsJsonObject();

            boolean isMatched = this.matchers.stream().anyMatch(matcher -> matcher.isMatched(item));
            if (isMatched) {
                result.add(this.parseListItem(item));
            }
        });

        return result;
    }

    private QuestListItemModel parseListItem(JsonObject jsonQuestListItem) {
        try {
            return QuestListItemModel.builder()
                    .name(jsonQuestListItem.get(NAME_FIELD_NAME).getAsString())
                    .pathToDescriptor(jsonQuestListItem.get(PATH_TO_DESCRIPTOR_FIELD_NAME).getAsString())
                    .chapterIndicatorFrom(jsonQuestListItem.has(CHAPTER_INDICATOR_FROM_FIELD_NAME) ?
                            jsonQuestListItem.get(CHAPTER_INDICATOR_FROM_FIELD_NAME).getAsInt() : null
                    )
                    .chapterIndicatorTo(jsonQuestListItem.has(CHAPTER_INDICATOR_TO_FIELD_NAME) ?
                            jsonQuestListItem.get(CHAPTER_INDICATOR_TO_FIELD_NAME).getAsInt() : null
                    )
                    .build();
        } catch (Exception e) {
            log.error("Can't parse QuestListItem.", e);
            return null;
        }
    }
}
