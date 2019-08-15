package com.alta.dao.domain.quest;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.quest.QuestModel;
import com.alta.dao.data.quest.QuestStepModel;
import com.alta.dao.domain.quest.matcher.QuestMatcher;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides the deserializer of quest json.
 */
public class QuestDeserializer implements JsonDeserializer<QuestModel> {

    public static final String NAME_FIELD_NAME = "name";
    public static final String DISPLAY_NAME_FIELD_NAME = "displayName";

    public static final String STEPS_FIELD_NAME = "steps";
    public static final String STEP_NUMBER_FIELD_NAME = "stepNumber";
    public static final String TRIGGER_TYPE_FIELD_NAME = "triggerType";
    public static final String TRIGGER_MAP_FIELD_NAME = "triggerMap";
    public static final String TARGET_UUID_FIELD_NAME = "targetUuid";

    public static final String EFFECTS_FIELD_NAME = "effects";
    public static final String BACKGROUND_EFFECTS_FIELD_NAME = "backgroundEffects";

    public static final String CHAPTER_INDICATOR_FROM_FIELD_NAME = "chapterIndicatorFrom";
    public static final String CHAPTER_INDICATOR_TO_FIELD_NAME = "chapterIndicatorTo";

    @Setter
    private List<QuestMatcher> stepMatchers;

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
    public QuestModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonQuestItem = json.getAsJsonObject();

        return QuestModel.builder()
                .name(jsonQuestItem.get(NAME_FIELD_NAME).getAsString())
                .displayName(jsonQuestItem.get(DISPLAY_NAME_FIELD_NAME).getAsString())
                .steps(this.parseSteps(jsonQuestItem.getAsJsonArray(STEPS_FIELD_NAME), context))
                .build();
    }

    private List<QuestStepModel> parseSteps(JsonArray stepsJsonArray, JsonDeserializationContext context) {
        if (stepsJsonArray == null || stepsJsonArray.size() == 0) {
            return Collections.emptyList();
        }

        List<QuestStepModel> steps = new ArrayList<>();
        stepsJsonArray.forEach(stepJsonItem -> {
            JsonObject item = stepJsonItem.getAsJsonObject();

            if (!this.isNeedToParseStep(item)) {
                return;
            }

            List<EffectDataModel> effects = context.deserialize(
                    item.getAsJsonArray(EFFECTS_FIELD_NAME), new TypeToken<ArrayList<EffectDataModel>>(){}.getType()
            );

            List<EffectDataModel> backgroundEffects;
            if (item.has(BACKGROUND_EFFECTS_FIELD_NAME)) {
                backgroundEffects = context.deserialize(
                        item.getAsJsonArray(BACKGROUND_EFFECTS_FIELD_NAME), new TypeToken<ArrayList<EffectDataModel>>(){}.getType()
                );
            } else {
                backgroundEffects = Collections.emptyList();
            }

            steps.add(
                    QuestStepModel.builder()
                            .stepNumber(item.get(STEP_NUMBER_FIELD_NAME).getAsInt())
                            .triggerType(item.get(TRIGGER_TYPE_FIELD_NAME).getAsString())
                            .triggerMap(item.get(TRIGGER_MAP_FIELD_NAME).getAsString())
                            .targetUuid(item.get(TARGET_UUID_FIELD_NAME).getAsString())
                            .chapterIndicatorFrom(item.get(CHAPTER_INDICATOR_FROM_FIELD_NAME).getAsInt())
                            .chapterIndicatorTo(item.get(CHAPTER_INDICATOR_TO_FIELD_NAME).getAsInt())
                            .effects(effects)
                            .backgroundEffects(backgroundEffects)
                            .build()
            );
        });

        return steps;
    }

    private boolean isNeedToParseStep(JsonObject stepJson) {
        if (this.stepMatchers == null || this.stepMatchers.isEmpty()) {
            return true;
        }

        return this.stepMatchers.stream().anyMatch(matcher -> matcher.isMatched(stepJson));
    }
}
