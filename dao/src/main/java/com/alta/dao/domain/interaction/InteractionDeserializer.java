package com.alta.dao.domain.interaction;

import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.interaction.*;
import com.alta.dao.data.interaction.postProcessing.InteractionPostProcessingModel;
import com.alta.dao.data.interaction.postProcessing.ProcessingType;
import com.alta.dao.data.interaction.postProcessing.UpdateFacilityVisibilityPostProcessModel;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.font.effects.Effect;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides custom matcher of interaction.
 */
@Slf4j
public class InteractionDeserializer implements JsonDeserializer<List<InteractionDataModel>> {

    private static final String UUID_FIELD_NAME = "uuid";
    private static final String TARGET_UUID_FIELD_NAME = "targetUuid";
    private static final String CHAPTER_INDICATOR_FROM_FIELD_NAME = "chapterIndicatorFrom";
    private static final String CHAPTER_INDICATOR_TO_FIELD_NAME = "chapterIndicatorTo";
    private static final String NEXT_INTERACTION_UUID_FIELD_NAME = "nextInteractionUuid";
    private static final String SHIFT_TILES_FIELD_NAME = "shiftTiles";
    private static final String X_FIELD_NAME = "x";
    private static final String Y_FIELD_NAME = "y";

    private static final String EFFECTS_FIELD_NAME = "effects";
    private static final String TYPE_FIELD_NAME = "type";

    private static final String PRE_CONDITION_FIELD_NAME = "preCondition";
    private static final String FAILED_PRE_CONDITION_EFFECTS_FIELD_NAME = "failedPreConditionEffects";
    private static final String FACILITY_UUID_FIELD_NAME = "facilityUuid";

    private static final String POST_PROCESSING_FIELD_NAME = "postProcessing";
    private static final String VALUE_FIELD_NAME = "value";

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
    public List<InteractionDataModel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonInteractions = json.getAsJsonArray();

        List<InteractionDataModel> result = new ArrayList<>();
        jsonInteractions.forEach(jsonInteractionItem -> {
            JsonObject item = jsonInteractionItem.getAsJsonObject();

            List<EffectDataModel> effects = context.deserialize(
                    item.getAsJsonArray(EFFECTS_FIELD_NAME), new TypeToken<ArrayList<EffectDataModel>>(){}.getType()
            );

            List<EffectDataModel> failedConditionEffects = context.deserialize(
                    item.getAsJsonArray(FAILED_PRE_CONDITION_EFFECTS_FIELD_NAME),
                    new TypeToken<ArrayList<EffectDataModel>>(){}.getType()
            );

            try {
                InteractionDataModel interactionDataModel = InteractionDataModel.builder()
                        .uuid(item.get(UUID_FIELD_NAME).getAsString())
                        .targetUuid(item.get(TARGET_UUID_FIELD_NAME).getAsString())
                        .chapterIndicatorFrom(item.has(CHAPTER_INDICATOR_FROM_FIELD_NAME) ?
                                item.get(CHAPTER_INDICATOR_FROM_FIELD_NAME).getAsInt() : null
                        )
                        .chapterIndicatorTo(item.has(CHAPTER_INDICATOR_TO_FIELD_NAME) ?
                                item.get(CHAPTER_INDICATOR_TO_FIELD_NAME).getAsInt() : null
                        )
                        .shiftTiles(this.parseShiftTiles(item.getAsJsonArray(SHIFT_TILES_FIELD_NAME)))
                        .nextInteractionUuid(item.has(NEXT_INTERACTION_UUID_FIELD_NAME) ?
                                item.get(NEXT_INTERACTION_UUID_FIELD_NAME).getAsString() : null
                        )
                        .preCondition(item.has(PRE_CONDITION_FIELD_NAME) ?
                                this.parsePreCondition(item.get(PRE_CONDITION_FIELD_NAME).getAsJsonObject()) : null
                        )
                        .failedPreConditionEffects(failedConditionEffects == null ? Collections.emptyList() : failedConditionEffects)
                        .effects(effects)
                        .postProcessors(this.parsePostProcessing(item.getAsJsonArray(POST_PROCESSING_FIELD_NAME)))
                        .build();
                result.add(interactionDataModel);
            } catch (Exception e) {
                String k = null;
            }

        });

        return result;
    }

    private List<Point> parseShiftTiles(JsonArray shiftTiles) {
        if (shiftTiles == null || shiftTiles.size() == 0) {
            return Collections.emptyList();
        }

        List<Point> result = new ArrayList<>();
        shiftTiles.forEach(shiftTile -> {
            JsonObject item = shiftTile.getAsJsonObject();
            result.add(new Point(
                    item.get(X_FIELD_NAME).getAsInt(),
                    item.get(Y_FIELD_NAME).getAsInt()
            ));
        });

        return result;
    }

    private List<InteractionPostProcessingModel> parsePostProcessing(JsonArray postProcessors) {
        if (postProcessors == null || postProcessors.size() == 0) {
            return Collections.emptyList();
        }

        List<InteractionPostProcessingModel> result = new ArrayList<>();
        postProcessors.forEach(jsonPostProcessItem -> {
            JsonObject item = jsonPostProcessItem.getAsJsonObject();

            ProcessingType type = ProcessingType.valueOf(item.get(TYPE_FIELD_NAME).getAsString());

            InteractionPostProcessingModel model = null;
            switch (type) {
                case UPDATE_FACILITY_VISIBILITY:
                    model = this.parseUpdateFacilityVisibilityProcessor(item);
                    break;
                default:
                    log.error("Unknown type of interaction post processor {}", type);
            }

            if (model != null) {
                result.add(model);
            }
        });

        return result;
    }

    private InteractionConditionModel parsePreCondition(JsonObject jsonObject) {
        return InteractionConditionModel.builder()
                .uuid(jsonObject.get(UUID_FIELD_NAME).getAsString())
                .conditionType(InteractionConditionModel.ConditionType.valueOf(
                        jsonObject.get(TYPE_FIELD_NAME).getAsString())
                )
                .build();
    }

    private UpdateFacilityVisibilityPostProcessModel parseUpdateFacilityVisibilityProcessor(JsonObject postProcessor) {
        try {
            UpdateFacilityVisibilityPostProcessModel postProcess = new UpdateFacilityVisibilityPostProcessModel();
            postProcess.setFacilityUuid(postProcessor.get(FACILITY_UUID_FIELD_NAME).getAsString());
            postProcess.setValue(postProcessor.get(VALUE_FIELD_NAME).getAsBoolean());
            return postProcess;
        } catch (Exception e) {
            log.error("Parsing of UpdateFacilityVisibilityPostProcessModel was failed with error: {}", e.getMessage());
            return null;
        }
    }
}
