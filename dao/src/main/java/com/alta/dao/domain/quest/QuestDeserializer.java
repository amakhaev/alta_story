package com.alta.dao.domain.quest;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.quest.QuestModel;
import com.alta.dao.data.quest.QuestStepModel;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides the deserializer of quest json.
 */
public class QuestDeserializer implements JsonDeserializer<QuestModel> {

    private static final String UUID_FIELD_NAME = "uuid";
    private static final String NAME_FIELD_NAME = "name";
    private static final String DISPLAY_NAME_FIELD_NAME = "displayName";

    private static final String STEPS_FIELD_NAME = "steps";
    private static final String STEP_NUMBER_FIELD_NAME = "stepNumber";
    private static final String TRIGGER_TYPE_FIELD_NAME = "triggerType";
    private static final String TRIGGER_MAP_FIELD_NAME = "triggerMap";
    private static final String TARGET_UUID_FIELD_NAME = "targetUuid";
    private static final String EFFECTS_FIELD_NAME = "effects";

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public QuestModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonQuestItem = json.getAsJsonObject();

        return QuestModel.builder()
                .uuid(jsonQuestItem.get(UUID_FIELD_NAME).getAsString())
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

            List<EffectDataModel> effects = context.deserialize(
                    item.getAsJsonArray(EFFECTS_FIELD_NAME), new TypeToken<ArrayList<EffectDataModel>>(){}.getType()
            );

            steps.add(
                    QuestStepModel.builder()
                            .stepNumber(item.get(STEP_NUMBER_FIELD_NAME).getAsInt())
                            .triggerType(item.get(TRIGGER_TYPE_FIELD_NAME).getAsString())
                            .triggerMap(item.get(TRIGGER_MAP_FIELD_NAME).getAsString())
                            .targetUuid(item.get(TARGET_UUID_FIELD_NAME).getAsString())
                            .effects(effects)
                            .build()
            );
        });

        return steps;
    }
}
